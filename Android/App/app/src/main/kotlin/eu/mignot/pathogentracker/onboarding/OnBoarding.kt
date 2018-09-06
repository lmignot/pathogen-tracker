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

  fun onChoosePrimaryActivity(s: SurveyType) {
    info("Primary activity: $s")
    vm.primaryActivity = s
    changePage(1)
  }

  fun onChooseSecondaryActivity(s: SurveyType) {
    info("Secondary activity: $s")
    vm.secondaryActivity = s
    changePage(2)
  }

  fun isComplete() {
    info("Completing onBoarding")
    vm.setOnBoardingComplete(true)
    startActivity<SurveysActivity>()
    finish()
  }

  fun getPermission(requestCode: Int) = when(requestCode) {
    UsesLocation.REQUEST_CODE ->
      askForLocationPermission(this, getString(R.string.request_permission_location))
    UsesCamera.REQUEST_CODE ->
      askForCameraPermission(this, getString(R.string.request_permission_camera))
    else -> info("We shouldn't be here...")
  }

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
        else -> info("We shouldn't be here...")
      }
    } else {
      info("permission was denied - need to handle this")
    }
  }

//  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

  private fun changePage(position: Int) {
    when (position) {
      0 -> changeFragment(
        ChoosePrimaryUserActivity.newInstance()
      )
      1 -> changeFragment(
        ChooseSecondaryUserActivity.newInstance(vm.primaryActivity)
      )
      2 -> changeFragment(
        if (EffortlessPermissions.hasPermissions(this, (UsesLocation.PERMISSION))) {
          return changePage(3)
        } else {
          AskForPermissionFragment.newInstance(
            UsesLocation.REQUEST_CODE,
            R.string.permission_location_rationale
          )
        }
      )
      3 -> changeFragment(
        if (EffortlessPermissions.hasPermissions(
            this, UsesCamera.CAMERA_PERMISSION, UsesCamera.STORAGE_PERMISSION)
        ) {
          return changePage(4)
        } else {
          AskForPermissionFragment.newInstance(
            UsesCamera.REQUEST_CODE,
            R.string.permission_camera_rationale
          )
        }
      )
      else -> changeFragment(
        EncryptionMessageFragment.newInstance()
      )
    }
  }

  private fun changeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
  }

}
