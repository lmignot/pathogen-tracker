package eu.mignot.pathogentracker.surveys.data.models.survey

import eu.mignot.pathogentracker.data.models.Location
import eu.mignot.pathogentracker.surveys.data.SurveyType
import java.util.*

interface Survey {
  val id: String
  val collectedOn: Calendar
  val locationCollected: Location
  val surveyType: SurveyType
}
