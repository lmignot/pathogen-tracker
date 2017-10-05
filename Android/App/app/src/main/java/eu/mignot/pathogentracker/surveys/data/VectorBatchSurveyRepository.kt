package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch

object VectorBatchSurveyRepository : SurveyRepository<VectorBatch> {

  override fun getSurvey(surveyId: String): VectorBatch? =
    VectorBatch().queryFirst { q -> q.equalTo( "id", surveyId ) }

  override fun getSurveys(): List<VectorBatch> = VectorBatch().queryAll()

}
