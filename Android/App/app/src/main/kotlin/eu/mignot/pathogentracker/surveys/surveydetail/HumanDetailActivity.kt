package eu.mignot.pathogentracker.surveys.surveydetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.*
import kotlinx.android.synthetic.main.activity_human_detail.*
import org.jetbrains.anko.startActivity

/**
 * View class for reviewing Human surveys
 */
class HumanDetailActivity : AppCompatActivity() {

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val survey by lazy {
    App.getLocalSurveysRepository().getSurvey(Human(), batchId)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<SurveysActivity>(
        AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_no_surveyId)
      )
    }
    setContentView(R.layout.activity_human_detail)
    setupToolbar(toolbarHD, getString(R.string.title_human_detail), R.drawable.arrow_back_white)
  }

  /**
   * Render the model's data to the view
   */
  override fun onResume() {
    super.onResume()
    survey?.let {
      humanSurveyDetailId.text = it.id
      humanSurveyDetailDate.text = it.collectedOn.formatTime()
      it.locationCollected?.let {
        loc -> humanSurveyDetailLoc.text = loc.toString()
      }
      it.dateOfBirth?.let {
        dob -> humanSurveyDetailDob.text = dob.formatDate()
      }
      it.gender?.let {
        gen -> humanSurveyDetailGender.text = gen
      }
      humanSurveyDetailIsPreggers.text = it.isPregnant.asYesOrNo()
      humanSurveyDetailSamples.text = it.samples.joinToString()
      humanSurveyDetailTravel.text = it.travelHistory.joinToString()
      humanSurveyDetailPastDiseases.text = it.pastInfections.joinToString()
      humanSurveyDetailIsFamilyIll.text = it.isFamilyMemberIll.asYesOrNo()
      humanSurveyDetailCurrentDiseases.text = it.currentInfections.fold("") { acc, s ->
          acc + s.toString() + "\n"
      }

    }
  }

}
