package eu.mignot.pathogentracker.surveys.data.models.ui

import eu.mignot.pathogentracker.surveys.data.SurveyType

data class UiSurvey (
  val surveyId: String,
  val surveyLocation: String,
  val surveyDate: String,
  val isUploaded: Boolean,
  val surveyType: SurveyType,
  val isFlagged: Boolean = false
)
