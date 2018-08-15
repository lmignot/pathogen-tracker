package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import eu.mignot.pathogentracker.surveys.data.models.database.Vector

object VectorSurveyRepository : SurveyRepository<Vector> {

  override fun getSurvey(surveyId: String): Vector? =
    Vector().queryFirst { equalTo( "id", surveyId ) }

  override fun getSurveys(): List<Vector> = Vector().queryAll()

  fun getSurveys(batchId: String): List<Vector> =
    Vector()
      .query { equalTo("batchId", batchId)}
      .sortedByDescending {
        it.sequence
      }

}
