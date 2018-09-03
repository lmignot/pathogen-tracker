package eu.mignot.pathogentracker.surveys.surveys

import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.data.models.ui.UiSurvey
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.formatTime
import org.jetbrains.anko.AnkoLogger

class SurveysViewModel(private val repository: SurveyRepository): AnkoLogger {

  /**
   * Fetches local surveys and maps them to [UiSurvey]
   * so both types can be displayed in the view
   */
  fun getSurveys(): List<UiSurvey> {
    // Check both repositories. User may switch back and forth between
    // collecting Patient and/or Mosquito surveys.
    val humans = repository
      .getSurveys(Human())
      .map {
        UiSurvey(
          it.id,
          it.locationCollected?.toString() ?: AppSettings.Constants.NO_VALUE,
          it.collectedOn.formatTime(),
          it.uploadedAt != null,
          SurveyType.PATIENT,
          it.isFlagged
        )
      }
    return humans.plus(
      repository.getSurveys(VectorBatch())
        .map {
          UiSurvey(
            it.id,
            it.locationCollected?.toString() ?: AppSettings.Constants.NO_VALUE,
            it.collectedOn.formatTime(),
            it.uploadedAt != null,
            SurveyType.VECTOR
          )
        }
    ).sortedByDescending {
      it.surveyDate
    }
  }

}
