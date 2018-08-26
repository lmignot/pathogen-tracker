package eu.mignot.pathogentracker.surveys.surveys

import eu.mignot.pathogentracker.surveys.data.UiSurveysRepository
import eu.mignot.pathogentracker.surveys.data.models.ui.UiSurvey
import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.formatTime
import org.jetbrains.anko.AnkoLogger

class SurveysViewModel(private val repository: UiSurveysRepository): AnkoLogger {

  fun getSurveys(): List<UiSurvey> {
    // Check both repositories as a user may switch back and forth between
    // collecting Patient or Mosquito surveys.
    val humans = repository
      .getHumanSurveys()
      .map {
        UiSurvey(
          it.id,
          it.locationCollected?.toString() ?: AppSettings.Constants.NO_VALUE,
          it.collectedOn.formatTime(),
          it.uploadedAt != null,
          SurveyType.PATIENT(),
          it.isFlagged
        )
      }
    return humans.plus(
      repository.getVectorBatchSurveys()
        .map {
          UiSurvey(
            it.id,
            it.locationCollected?.toString() ?: AppSettings.Constants.NO_VALUE,
            it.collectedOn.formatTime(),
            it.uploadedAt != null,
            SurveyType.VECTOR()
          )
        }
    ).sortedByDescending {
      it.surveyDate
    }
  }

}
