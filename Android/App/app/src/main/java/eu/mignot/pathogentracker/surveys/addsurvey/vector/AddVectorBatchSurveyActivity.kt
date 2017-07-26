package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.text.format.DateFormat
import android.view.View
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.LocationProviderImpl
import eu.mignot.pathogentracker.data.models.Location
import eu.mignot.pathogentracker.extensions.showShortMessage
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_vector_batch_survey.*
import org.jetbrains.anko.debug
import java.util.*

class AddVectorBatchSurveyActivity: BaseSurveyActivity() {

  private val vm by lazy {
    AddVectorBatchViewModel(LocationProviderImpl)
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
    // set the location listener
    setLocationListener()
  }

  override fun unbind() {
    disposables.clear()
  }

  override fun saveAndClose() {
    debug("saving this shiz...")
    showShortMessage(vectorBatchForm, "Saving...")
  }

  private fun getId() = batchId?.setText(vm.id)

  private fun getDate() = batchDate?.setText(DateFormat.format(R.string.date_format.toString(), vm.date))

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
            },
            onSuccess = {
              location = it
              batchLocation?.setText(it.toString())
              batchLocationProgress?.visibility = View.GONE
            }
          )
      )
    }
  }
}
