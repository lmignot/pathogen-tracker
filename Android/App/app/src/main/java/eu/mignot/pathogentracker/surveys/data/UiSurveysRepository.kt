package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.queryAll
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch

object UiSurveysRepository {

  fun getHumanSurveys(): List<Human> = Human().queryAll()

  fun getVectorBatchSurveys(): List<VectorBatch> = VectorBatch().queryAll()

}
