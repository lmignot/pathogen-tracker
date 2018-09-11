package eu.mignot.pathogentracker.surveys.surveydetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.asYesOrNo
import eu.mignot.pathogentracker.util.setupToolbar
import kotlinx.android.synthetic.main.activity_vector_detail.*
import org.jetbrains.anko.startActivity

class VectorDetailActivity : AppCompatActivity() {

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_BATCH_ID_VALUE
  }

  private val surveyId by lazy {
    intent.getStringExtra(AppSettings.Constants.VECTOR_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val survey by lazy {
    App.getLocalSurveysRepository().getSurvey(Vector(), surveyId)
  }

  private val photoRepository by lazy {
    App.getLocalPhotoRepository()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (batchId == AppSettings.Constants.NO_BATCH_ID_VALUE) {
      startActivity<SurveysActivity>()
    }
    if (surveyId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<VectorBatchDetailActivity>(
        AppSettings.Constants.BATCH_ID_KEY to batchId
      )
    }
    setContentView(R.layout.activity_vector_detail)
    setupToolbar(toolbarVD, getString(R.string.title_vector_detail), R.drawable.arrow_back_white)
  }

  override fun onResume() {
    super.onResume()
    survey?.let {
      vectorDetailId.text = getString(
        R.string.survey_vector_item_id,
        it.id,
        String.format("%03d",it.sequence)
      )
      vectorDetailSpecies.text = it.species
      vectorDetailGender.text = it.gender
      vectorDetailStage.text = it.stage
      vectorDetailDidFeed.text = it.didFeed.asYesOrNo()
      it.photo?.let { p ->
        val photo = photoRepository.retrievePhoto(p.path)
        vectorDetailPhoto.setImageBitmap(photo)
        vectorDetailPhoto.visibility = View.VISIBLE
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        startActivity<VectorBatchDetailActivity>(
          AppSettings.Constants.BATCH_ID_KEY to batchId
        )
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

}
