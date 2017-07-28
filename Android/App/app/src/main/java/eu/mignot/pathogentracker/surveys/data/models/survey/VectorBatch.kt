package eu.mignot.pathogentracker.surveys.data.models.survey

import eu.mignot.pathogentracker.data.models.Location
import eu.mignot.pathogentracker.surveys.data.SurveyType
import java.util.*

data class VectorBatch (
  override val id: String,
  override val collectedOn: Calendar,
  override val locationCollected: Location,
  val territory: String,
  val temperature: Int,
  val weatherCondition: String,
  val vectors: List<String> = listOf(),
  override val surveyType: SurveyType = SurveyType.VECTOR
): Survey
