package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import io.realm.RealmObject

object RealmSurveysRepository: SurveyRepository {

  override fun getPatientSurvey(surveyId: String): Human? =
    Human().queryFirst { equalTo("id", surveyId) }

  override fun getPatientSurveys(): List<Human> = Human().queryAll()

  override fun getVectorBatchSurveys(): List<VectorBatch> = VectorBatch().queryAll()

  override fun getVectorBatchSurvey(surveyId: String): VectorBatch? =
    VectorBatch().queryFirst { equalTo( "id", surveyId ) }

  override fun getVectorSurvey(surveyId: String) =
    Vector().queryFirst { equalTo( "id", surveyId ) }

  override fun getVectorSurveys(): List<Vector> = Vector().queryAll()

  override fun getVectorSurveysFor(parentId: String): List<Vector> = Vector()
    .query { equalTo("batchId", parentId) }
    .sortedByDescending {
      it.sequence
    }

  override fun getPhotosToUpload(): List<Photo> = Photo()
    .query { isNull("uploadedAt") }

  override fun getVectorsToUpload(): List<Vector> = Vector()
    .query { isNull("uploadedAt") }

  override fun getVectorBatchesToUpload(): List<VectorBatch> = VectorBatch()
    .query { isNull("uploadedAt") }

  override fun getHumansToUpload(): List<Human> = Human()
    .query { isNull("uploadedAt") }

  fun <T: RealmObject> saveSurvey(survey: T) = survey.save()

}
