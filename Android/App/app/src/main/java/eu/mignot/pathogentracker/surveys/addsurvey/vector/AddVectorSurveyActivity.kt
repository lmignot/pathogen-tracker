package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.os.Bundle
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.UsesCamera
import eu.mignot.pathogentracker.util.AppSettings
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.activity_add_vector_survey.*
import kotlinx.android.synthetic.main.vector_form.*

class AddVectorSurveyActivity: BaseSurveyActivity(), UsesCamera, AnkoLogger {

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val vm by lazy {
    AddVectorViewModel(batchId, batchId + "-001")
  }

  private val disposables by lazy {
    CompositeDisposable()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<MainActivity>(AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_vector_no_batchid))
    }
    setContentView(R.layout.activity_add_vector_survey)
    setupToolbar()
  }

  override fun bind() {
    info("binding...")
  }

  override fun unbind() {
    disposables.clear()
  }

  override fun setupToolbar() {
    setSupportActionBar(toolbarAV)
    supportActionBar?.setTitle(R.string.add_vector)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.close_white)
  }

  override fun saveAndClose() {
    info("closing...")
  }

}
