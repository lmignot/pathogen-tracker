package eu.mignot.pathogentracker.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.data.models.remote.*
import eu.mignot.pathogentracker.util.AppSettings
import io.realm.RealmObject
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class FirebaseSurveysRepository(
  private val db: FirebaseFirestore
): SurveyRepository, AnkoLogger {

  /**
   * @see SurveyRepository.getSurvey
   */
  override fun <T : RealmObject> getSurvey(surveyType: T, surveyId: String): T? =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  /**
   * @see SurveyRepository.getSurveys
   */
  override fun <T : RealmObject> getSurveys(surveyType: T): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  /**
   * @see SurveyRepository.getSurveysToSync
   */
  override fun <T : RealmObject> getSurveysToSync(surveyType: T): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  /**
   * @see SurveyRepository.getSurveysFor
   */
  override fun <T : RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  /**
   * @see SurveyRepository.storeSurvey
   */
  override fun <T: RealmObject> storeSurvey(survey: T) {
    when(survey) {
      is Human -> uploadHumanSurvey(survey)
      is VectorBatch -> uploadVectorBatchSurvey(survey)
      is Vector -> uploadVectorSurvey(survey)
      else -> info { "Unexpected survey type" }
    }
  }

  /**
   * Transmits a [VectorBatch] survey to Cloud Firestore
   *
   * @param survey the [VectorBatch] survey to upload
   */
  private fun uploadVectorBatchSurvey(survey: VectorBatch) {
    // map the model for Firestore
    val model = with(survey) {
      RemoteVectorBatch(
        id, collectedOn,
        with(locationCollected) {
          this?.let {
            RemoteLocation(it.longitude, it.latitude, it.accuracy)
          }
        },
        territory, temperature, weatherCondition
      )
    }
    db.collection(AppSettings.Constants.VECTOR_BATCH_COLLECTION)
      .document(survey.id)
      .set(model)
      .addOnCompleteListener {
        survey.uploadedAt = Date()
        survey.save()
        logUploadSuccess(survey.id)
      }
      .addOnFailureListener{
        logUploadFailure(survey.id, it)
      }
  }

  /**
   * Transmits a [Vector] survey to Cloud Firestore
   *
   * @param survey the [Vector] survey to upload
   */
  private fun uploadVectorSurvey(survey: Vector) {
    // map the model for Firestore
    val model = with(survey) {
      RemoteVector(
        id, batchId, sequence, species, gender, stage, didFeed, photo?.fileName
      )
    }
    db.collection(AppSettings.Constants.VECTOR_COLLECTION)
      .document(survey.id)
      .set(model)
      .addOnCompleteListener {
        survey.uploadedAt = Date()
        survey.save()
        logUploadSuccess(survey.id)
      }
      .addOnFailureListener{
        logUploadFailure(survey.id, it)
      }
  }

  /**
   * Transmits a [Human] survey to Cloud Firestore
   *
   * @param survey the [Human] survey to upload
   */
  private fun uploadHumanSurvey(survey: Human) {
    // map the model for Firestore
    val model = with(survey) {
      RemoteHuman(
        id, collectedOn,
        with(locationCollected) {
          this?.let {
            RemoteLocation(it.longitude, it.latitude, it.accuracy)
          }
        },
        dateOfBirth, gender, isPregnant, isFamilyMemberIll, isFlagged, samples.map{it.name},
        travelHistory.map{it.name},
        pastInfections.map {it.name},
        currentInfections.map {
          RemoteInfection(it.name, it.symptoms.map{s -> s.name})
        }
      )
    }
    db.collection(AppSettings.Constants.HUMAN_COLLECTION)
      .document(survey.id)
      .set(model)
      .addOnCompleteListener {
        survey.uploadedAt = Date()
        survey.save()
        logUploadSuccess(survey.id)
      }
      .addOnFailureListener{
        logUploadFailure(survey.id, it)
      }
  }

  /**
   * Helper method to log success
   *
   * @param id The survey id
   */
  private fun logUploadSuccess(id: String) {
    info { "Successfully uploaded survey: $id" }
  }

  /**
   * Helper method to log upload error
   *
   * @param id The survey id
   * @param err The exception thrown by the upload process
   */
  private fun logUploadFailure(id: String, err: Exception) {
    error { "Error uploading survey: $id \n ${err.localizedMessage}" }
  }

  companion object {
    private const val NOT_IMPLEMENTED_RATIONALE = AppSettings.Constants.REPOSITORY_NI_RATIONALE
  }

}
