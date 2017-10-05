package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.util.asIntOrDefault
import eu.mignot.pathogentracker.util.selectedValue
import eu.mignot.pathogentracker.util.setupToolbar
import eu.mignot.pathogentracker.util.showShortMessage
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.data.models.database.Location
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.UsesLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_vector_batch_survey.*
import kotlinx.android.synthetic.main.vector_batch_form.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.*

class AddVectorBatchSurveyActivity: BaseSurveyActivity<VectorBatch>(), UsesLocation {

  private val temperatures by lazy {
    (AppSettings.Constants.MIN_TEMP..AppSettings.Constants.MAX_TEMP)
      .toList().reversed().map { it.toString() }
  }

  override val vm by lazy {
    AddVectorBatchViewModel(
      App.getLocationProvider(),
      App.getVectorBatchRepository()
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_vector_batch_survey)
    setupToolbar(toolbarAVB, getString(R.string.title_vector_batch), R.drawable.close_white)
  }

  override fun bind() {
    // get the id
    getId()
    // get the generated date
    getDate()
    // set the date listener
    setDateListener()
    // check for location permissions (sets listener if successful)
    onRequestLocationPermission()
    // set value to model automatically
    batchTerritory?.onCheckedChange { r, _ ->
      r?.selectedValue()?.let {
        vm.territory = it
      }
    }
    // show a nice spinner for the temps :)
    batchTemperature?.setOnClickListener {
      selector("Choose temperature", temperatures, {_, i ->
        batchTemperature.setText(temperatures[i])
      })
    }
  }

  override fun getModel(): VectorBatch {
    val model = VectorBatch()
    model.id = vm.id
    model.collectedOn = vm.date.time
    model.locationCollected = vm.location?.let {
      Location(it.longitude, it.latitude, it.accuracy)
    }
    model.temperature = batchTemperature.asIntOrDefault(0)
    model.weatherCondition = batchWeatherConditions.text.toString()
    model.territory = vm.territory?.let { it }
    return model
  }

  override fun saveAndClose() {
    val model = getModel()
    info(model)
    doAsync {
      vm.save(model)
      uiThread {
        startActivity<SurveysActivity>(
          AppSettings.Constants.MESSAGE_KEY to "Survey added to database"
        )
        finish()
      }
    }
  }

  private fun getId() = batchId?.setText(vm.id)

  private fun getDate() = batchDate?.setText(DateFormat.format("dd MMMM yyyy", vm.date))

  private fun setDateListener() {
    batchDate?.setOnClickListener {
      val date = vm.date
      DatePickerDialog(
        this,
        DatePickerDialog.OnDateSetListener({_, year, month, dayOfMonth ->
          date.set(Calendar.YEAR, year)
          date.set(Calendar.MONTH, month)
          date.set(Calendar.DATE, dayOfMonth)
          vm.date = date
          getDate()
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
          yesButton {
            startActivity<SurveysActivity>()
            // TODO: launch the settings app
          }
        }.show()
        else -> askForLocationPermission(
          this,
          "We need permission to access this device's location"
        )
    }
  }

  @AfterPermissionDenied(UsesLocation.REQUEST_CODE)
  override fun onLocationPermissionDenied() {
    askForLocationPermission(
      this,
      "This app cannot function without access to this device's location, please grant permission"
    )
  }

  override fun setLocationListener() {
    batchLocation?.setOnClickListener {
      batchLocationProgress?.visibility = View.VISIBLE
      disposables.add(
        vm
          .getLocation()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy (
            onError = {
              showShortMessage(vectorBatchForm, "Unable to determine location, please try again")
              batchLocationProgress?.visibility = View.GONE
              error { it.localizedMessage }
            },
            onNext = {
              vm.location = it
              batchLocation?.setText(it.toString())
              batchLocationProgress?.visibility = View.GONE
            }
          )
      )
    }
  }
}
