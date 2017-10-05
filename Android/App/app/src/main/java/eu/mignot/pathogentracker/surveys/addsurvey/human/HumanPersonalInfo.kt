package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.app.DatePickerDialog
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.View
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.util.asBoolean
import eu.mignot.pathogentracker.util.selectedValue
import eu.mignot.pathogentracker.util.showShortMessage
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.Location
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.UsesLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_info.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.*

class HumanPersonalInfo: StepFragment(), UsesLocation {

  override val layoutResourceId: Int = R.layout.fragment_personal_info

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
    onRequestLocationPermission()
    // update ViewModel when selection is made
    patientIsPregnant?.onCheckedChange { r, _ ->
      r?.asBoolean()?.let {
        vm.isPregnant = it
        info(vm.isPregnant)
      }
    }
    patientGender?.onCheckedChange { r, _ ->
      r?.selectedValue()?.let {
        vm.gender = it
        info(vm.gender)
      }
    }
  }

  override fun getModel(model: Human): Human {
    model.id = vm.id
    model.collectedOn = vm.date.time
    model.locationCollected = vm.location?.let {
      Location(it.longitude, it.latitude, it.accuracy)
    }
    model.dateOfBirth = vm.dateOfBirth.time
    model.gender = vm.gender
    model.isPregnant = vm.isPregnant
    return model
  }

  private fun getDateCollected() = dateCollected?.setText(DateFormat.format("dd MMMM yyyy", vm.date))

  private fun setDateCollectedListener() {
    dateCollected?.setOnClickListener {
      val date = vm.date
      DatePickerDialog(
        activity,
        DatePickerDialog.OnDateSetListener({ _, year, month, dayOfMonth ->
          date.set(Calendar.YEAR, year)
          date.set(Calendar.MONTH, month)
          date.set(Calendar.DATE, dayOfMonth)
          vm.date = date
          getDateCollected()
        }),
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DATE)
      ).show()
    }
  }

  private fun getDateOfBirth() = dateOfBirth?.setText(DateFormat.format("dd MMMM yyyy", vm.dateOfBirth))

  private fun setDateOfBirthListener() {
    dateOfBirth?.setOnClickListener {
      val date = vm.dateOfBirth
      DatePickerDialog(
        activity,
        DatePickerDialog.OnDateSetListener({ _, year, month, dayOfMonth ->
          date.set(Calendar.YEAR, year)
          date.set(Calendar.MONTH, month)
          date.set(Calendar.DATE, dayOfMonth)
          vm.dateOfBirth = date
          getDateOfBirth()
        }),
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DATE)
      ).show()
    }
  }

  @AfterPermissionGranted(UsesLocation.REQUEST_CODE)
  override fun onRequestLocationPermission() {
    when {
      EffortlessPermissions.hasPermissions(this, UsesLocation.PERMISSION) -> setLocationListener()
      EffortlessPermissions.permissionPermanentlyDenied(this, UsesLocation.PERMISSION) ->
        alert("Please grant permission in the settings app and try again", "Access to location is required") {
          yesButton { startActivity<SurveysActivity>() }
        }.show()
      else -> askForLocationPermission(
        activity,
        "We need permission to access this device's location"
      )
    }
  }

  @AfterPermissionDenied(UsesLocation.REQUEST_CODE)
  override fun onLocationPermissionDenied() {
    askForLocationPermission(
      activity,
      "This app cannot function without access to this device's location, please grant permission"
    )
  }

  override fun setLocationListener() {
    surveyLocation?.setOnClickListener {
      surveyLocationProgress?.visibility = View.VISIBLE
      (activity as AddHumanSurveyActivity).disposables.add(
        vm
          .getLocation()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy (
            onError = {
              showShortMessage(personalInfoForm, "Unable to determine location, please try again")
              surveyLocationProgress?.visibility = View.GONE
              error { it.localizedMessage }
            },
            onNext = {
              vm.location = it
              surveyLocation?.setText(it.toString())
              surveyLocationProgress?.visibility = View.GONE
            }
          )
      )
    }
  }

}
