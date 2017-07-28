package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.text.format.DateFormat
import android.view.View
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.Location
import eu.mignot.pathogentracker.extensions.asIntOrDefault
import eu.mignot.pathogentracker.extensions.showShortMessage
import eu.mignot.pathogentracker.extensions.asString
import eu.mignot.pathogentracker.extensions.selectedValue
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.UsesLocation
import eu.mignot.pathogentracker.surveys.data.models.survey.VectorBatch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_vector_batch_survey.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.*
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.*

class AddVectorBatchSurveyActivity: BaseSurveyActivity(), UsesLocation, AnkoLogger {

  private val vm by lazy {
    AddVectorBatchViewModel(App.getLocationProvider())
  }

  private val disposables by lazy {
    CompositeDisposable()
  }

  private var location: Location = Location()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_vector_batch_survey)
    setupToolbar()
  }

  override fun setupToolbar() {
    setSupportActionBar(toolbarAVB)
    supportActionBar?.setTitle(R.string.vector_batch)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.close_white)
  }

  override fun onCreateOptionsMenu(menu: Menu):Boolean {
    menuInflater.inflate(R.menu.add_survey_menu_white, menu)
    return true
  }

  override fun onResume() {
    super.onResume()
  }

  override fun bind() {
    // get the id
    getId()
    // get the generated date
    getDate()
    // set the date listener
    setDateListener()
    // check for location permissions (sets listener if successful)
    requestLocationPermission()
  }

  override fun unbind() {
    disposables.clear()
  }

  override fun saveAndClose() {
    val progress = indeterminateProgressDialog("Saving. Please wait...")
    val model = VectorBatch(vm.id, vm.date, location, batchTerritory.selectedValue(),
      batchTemperature.asIntOrDefault(0), batchWeatherConditions.asString())
    info { model }
    disposables
      .add(
        vm.save(model)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy (
            onError = {
              progress.hide()
              alert ("There was an error saving, try again?") {
                yesButton {
                  saveAndClose()
                }
                noButton {
//                  startActivity<MainActivity>()
//                  finish()
                }
              }
            },
            onSuccess = {
              progress.hide()
              showShortMessage(vectorBatchForm, "Successfully saved")
//              startActivity<MainActivity>()
//              finish()
            }
          )
      )
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
  override fun requestLocationPermission() {
    if (EffortlessPermissions.hasPermissions(this, UsesLocation.PERMISSION)) {
      setLocationListener()
    } else if (EffortlessPermissions.permissionPermanentlyDenied(this, UsesLocation.PERMISSION)) {
      alert("Device location required", "Please grant permission in the settings app and try again") {
        yesButton { startActivity<MainActivity>() }
      }.show()
    } else {
      askForLocationPermission(
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

  private fun setLocationListener() {
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
              location = it
              batchLocation?.setText(it.toString())
              batchLocationProgress?.visibility = View.GONE
            }
          )
      )
    }
  }
}
