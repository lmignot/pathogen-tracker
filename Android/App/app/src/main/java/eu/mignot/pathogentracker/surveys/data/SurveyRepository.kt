package eu.mignot.pathogentracker.surveys.data

import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch

interface SurveyRepository {

  fun getPatientSurvey(surveyId: String): Human?

  fun getPatientSurveys(): List<Human>

  fun getVectorBatchSurvey(surveyId: String): VectorBatch?

  fun getVectorBatchSurveys(): List<VectorBatch>

  fun getVectorSurvey(surveyId: String): Vector?

  fun getVectorSurveys(): List<Vector>

  fun getVectorSurveysFor(parentId: String): List<Vector>

  fun getPhotosToUpload(): List<Photo>

  fun getVectorsToUpload(): List<Vector>

  fun getVectorBatchesToUpload(): List<VectorBatch>

  fun getHumansToUpload(): List<Human>

}
