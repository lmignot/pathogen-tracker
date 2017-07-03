package eu.mignot.pathogentracker.surveys.data.models.survey

import eu.mignot.pathogentracker.common.util.SurveyType
import java.util.*

interface Survey {
  val id: String
  val dateCollected: Date
  val locationCollected: Location
  val lastUpdated: Date
  val surveyType: SurveyType
}
