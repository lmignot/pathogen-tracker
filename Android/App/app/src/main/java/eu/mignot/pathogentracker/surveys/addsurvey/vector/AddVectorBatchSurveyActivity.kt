package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.DatePickerDialog
import android.os.Bundle
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
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.data.models.database.Location
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import eu.mignot.pathogentracker.surveys.data.models.ui.UiLocation
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.*
import kotlinx.android.synthetic.main.activity_add_vector_batch_survey.*
import kotlinx.android.synthetic.main.vector_batch_form.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import java.util.*

class AddVectorBatchSurveyActivity: BaseSurveyActivity<VectorBatch>(), LocationView {

  private val temperatures by lazy {
    (AppSettings.Constants.MIN_TEMP..AppSettings.Constants.MAX_TEMP)
      .toList().reversed().map { it.toString() }
  }

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

  override val vm by lazy {
    AddVectorBatchViewModel(
      App.getSurveysRepository()
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_vector_batch_survey)
    setupToolbar(toolbarAVB, getString(R.string.title_vector_batch), R.drawable.close_white)
  }

  override fun bind() {
    getId()
    getDate()
    setDateListener()
    setLocationListener()

    // set territory value to vm when changed
    batchTerritory?.onCheckedChange { r, _ ->
      r?.selectedValue()?.let {
        vm.territory = it
      }
    }

    // show temperature selector
    batchTemperature?.setOnClickListener {
      selector("Choose temperature", temperatures, {_, i ->
        batchTemperature.setText(temperatures[i])
      })
    }

    if (getLocationManager().isWaitingForLocation && !getLocationManager().isAnyDialogShowing) {
      showLocationProgress()
    }
  }

  override fun unbind() {
    dismissLocationProgress()
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

  private fun setLocationListener() {
    batchLocation?.setOnClickListener { getLocation() }
  }

  override fun getLocationManager(): LocationManager {
    return LocationManager.Builder(App.instance)
      .activity(this)
      .configuration(locationConfiguration)
      .notify(this)
      .build()
  }

  override fun gpsPermissionGranted(alreadyHadPermission: Boolean) {
    if (!alreadyHadPermission) {
      showShortMessage(vectorBatchForm, "Thanks for granting permission, tap the location field to get the current location")
    }
  }

  override fun processLocation(location: android.location.Location?) {
    location?.let {
      val loc = UiLocation(it.longitude, it.latitude, it.accuracy)
      vm.location = loc
      batchLocation?.setText(loc.toString())
    }
  }

  override fun processLocationError(type: Int) {
    showShortMessage(vectorBatchForm, "Unable to determine location, please try again")
  }

  override fun dismissLocationProgress() {
    batchLocationProgress?.visibility = View.GONE
  }

  override fun showLocationProgress() {
    batchLocationProgress?.visibility = View.GONE
  }

}
