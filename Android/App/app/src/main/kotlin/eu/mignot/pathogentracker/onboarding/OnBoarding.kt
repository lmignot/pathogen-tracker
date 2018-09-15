package eu.mignot.pathogentracker.onboarding

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.UsesCamera
import eu.mignot.pathogentracker.util.UsesLocation
import eu.mignot.pathogentracker.util.showShortMessage
import kotlinx.android.synthetic.main.activity_onboarding.*
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

class OnBoarding : AppCompatActivity(), AnkoLogger, UsesCamera, UsesLocation {

  private val vm by lazy {
    OnBoardingViewModel(App.getPreferenceProvider())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_onboarding)
    changePage(0)
  }

  // prevent the user from pressing the back button during onboarding
  override fun onBackPressed() {
    info { "Back button pressed in onboarding" }
  }

  // set the chosen survey type in the vm
  fun onChoosePrimaryActivity(s: SurveyType) {
    info("Primary activity: $s")
    vm.primaryActivity = s
    changePage(1)
  }

  // set the secondary survey type in the vm
  fun onChooseSecondaryActivity(s: SurveyType) {
    info("Secondary activity: $s")
    vm.secondaryActivity = s
    changePage(2)
  }

  // set on-boarding to complete
  fun isComplete() {
    info("Completing onBoarding")
    vm.setOnBoardingComplete(true)
    startActivity<SurveysActivity>()
    finish()
  }

  /**
   * Request the appropriate permission
   */
  fun requestPermission(code: Int) = when(code) {
    UsesLocation.REQUEST_CODE ->
      askForLocationPermission(this, getString(R.string.request_permission_location))
    UsesCamera.REQUEST_CODE ->
      askForCameraPermission(this, getString(R.string.request_permission_camera))
    else -> info("Unexpected code")
  }

  /**
   * Handle the result of a user granting or denying permissions
   */
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      when (requestCode) {
        UsesLocation.REQUEST_CODE -> {
          info("Requested location permission")
          changePage(3)
        }
        UsesCamera.REQUEST_CODE -> {
          info("Requested camera permission")
          changePage(4)
        }
        else -> info("Unexpected condition")
      }
    } else {
      showShortMessage(onBoardingRoot, getString(R.string.error_permissions_denied))
    }
  }

  /**
   * Change the view fragment
   *
   * @param page the desired page that should be displayed
   */
  private fun changePage(page: Int) {
    when (page) {
      0 -> changeFragment(
        ChoosePrimarySurvey.newInstance()
      )
      1 -> changeFragment(
        ChooseSecondarySurvey.newInstance(vm.primaryActivity)
      )
      2 -> changeFragment(
        // don't show permissions request if already granted
        if (EffortlessPermissions.hasPermissions(this, (UsesLocation.PERMISSION))) {
          return changePage(3)
        } else {
          AskForPermission.newInstance(
            UsesLocation.REQUEST_CODE,
            R.string.permission_location_rationale
          )
        }
      )
      3 -> changeFragment(
        // don't show permissions request if already granted
        if (EffortlessPermissions.hasPermissions(
            this, UsesCamera.CAMERA_PERMISSION, UsesCamera.STORAGE_PERMISSION)
        ) {
          return changePage(4)
        } else {
          AskForPermission.newInstance(
            UsesCamera.REQUEST_CODE,
            R.string.permission_camera_rationale
          )
        }
      )
      else -> changeFragment(
        EncryptionMessage.newInstance()
      )
    }
  }

  /**
   * Replaces the current fragment
   *
   * @param fragment the fragment to display
   */
  private fun changeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
  }

}
