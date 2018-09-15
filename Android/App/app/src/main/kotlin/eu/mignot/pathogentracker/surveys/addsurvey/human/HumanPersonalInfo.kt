package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.app.DatePickerDialog
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.View
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.configuration.PermissionConfiguration
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.LocationView
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.Location
import eu.mignot.pathogentracker.data.models.ui.UiLocation
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.asBoolean
import eu.mignot.pathogentracker.util.selectedValue
import eu.mignot.pathogentracker.util.showShortMessage
import kotlinx.android.synthetic.main.fragment_personal_info.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import java.util.*

class HumanPersonalInfo: StepFragment(), LocationView {

  override val layoutResourceId: Int = R.layout.fragment_personal_info

  private val locationConfiguration by lazy {
    LocationConfiguration.Builder()
      .keepTracking(false)
      .askForPermission(
        PermissionConfiguration.Builder()
          .rationaleMessage("This app requires access to your device's location").build()
      )
      .useGooglePlayServices(GooglePlayServicesConfiguration.Builder().build())
      .useDefaultProviders(
        DefaultProviderConfiguration.Builder()
          .gpsMessage("Please turn on location services").build()
      )
      .build()
  }

  /**
   * @see Fragment.onResume
   */
  override fun onResume() {
    super.onResume()
    // get the id
    patientId?.setText(vm.id)
    // get the generated date
    getDateCollected()
    // set date listeners
    setDateCollectedListener()
    setDateOfBirthListener()
    // check for location permissions (sets listener if successful)
    surveyLocation?.setOnClickListener { getLocation() }
    // update ViewModel when selection is made
    patientIsPregnant?.onCheckedChange { r, _ ->
      r?.asBoolean()?.let { vm.isPregnant = it }
    }
    patientGender?.onCheckedChange { r, _ ->
      r?.selectedValue()?.let { vm.gender = it }
      r?.selectedValue()?.let {
        when(it) {
          "Male" -> {
            patientIsPregnant.getChildAt(0).isEnabled = false
            patientIsPregnant.check(patientIsPregnant.getChildAt(1).id)
          }
          "Female" -> {
            patientIsPregnant.getChildAt(0).isEnabled = true
          }
        }
      }
    }
    if (getLocationManager().isWaitingForLocation && !getLocationManager().isAnyDialogShowing) {
      showLocationProgress()
    }
  }

  override fun onPause() {
    super.onPause()
    dismissLocationProgress()
  }

  override fun getModel(model: Human): Human {
    model.id = vm.id
    model.collectedOn = vm.date.time
    model.locationCollected = vm.location?.let {
      Location(
        it.longitude,
        it.latitude,
        it.accuracy
      )
    }
    model.dateOfBirth = vm.dateOfBirth.time
    model.gender = vm.gender
    model.isPregnant = vm.isPregnant
    return model
  }

  private fun getDateCollected() = dateCollected?.setText(DateFormat.format(AppSettings.Constants.DATE_FORMAT, vm.date))

  private fun setDateCollectedListener() {
    dateCollected?.setOnClickListener {
      val date = vm.date
      DatePickerDialog(
        activity,
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
          date.set(Calendar.YEAR, year)
          date.set(Calendar.MONTH, month)
          date.set(Calendar.DATE, dayOfMonth)
          vm.date = date
          getDateCollected()
        },
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DATE)
      ).show()
    }
  }

  private fun getDateOfBirth() = dateOfBirth?.setText(DateFormat.format(AppSettings.Constants.DATE_FORMAT, vm.dateOfBirth))

  private fun setDateOfBirthListener() {
    dateOfBirth?.setOnClickListener {
      val date = vm.dateOfBirth
      DatePickerDialog(
        activity,
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
          date.set(Calendar.YEAR, year)
          date.set(Calendar.MONTH, month)
          date.set(Calendar.DATE, dayOfMonth)
          vm.dateOfBirth = date
          getDateOfBirth()
        },
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DATE)
      ).show()
    }
  }

  override fun getLocationManager(): LocationManager {
    return LocationManager.Builder(App.instance)
      .activity(activity!!)
      .fragment(this)
      .configuration(locationConfiguration)
      .notify(this)
      .build()
  }

  override fun gpsPermissionGranted(alreadyHadPermission: Boolean) {
    if (!alreadyHadPermission) {
      showShortMessage(personalInfoForm, getString(R.string.thanks_for_loc_perm))
    }
  }

  override fun processLocation(location: android.location.Location?) {
    location?.let {
      val loc =
        UiLocation(it.longitude, it.latitude, it.accuracy)
      vm.location = loc
      surveyLocation?.setText(loc.toString())
    }
  }

  override fun processLocationError(type: Int) {
    showShortMessage(personalInfoForm, getString(R.string.error_loc_retrieval))
  }

  override fun dismissLocationProgress() {
    surveyLocationProgress?.visibility = View.GONE
  }

  override fun showLocationProgress() {
    surveyLocationProgress?.visibility = View.GONE
  }

}
