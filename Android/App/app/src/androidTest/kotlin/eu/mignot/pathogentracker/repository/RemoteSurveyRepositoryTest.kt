package eu.mignot.pathogentracker.repository

import android.support.test.runner.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.TestCommon.createHuman
import eu.mignot.pathogentracker.TestCommon.createVector
import eu.mignot.pathogentracker.TestCommon.createVectorBatch
import eu.mignot.pathogentracker.util.AppSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RemoteSurveyRepositoryTest {

  private fun fireStoreInstance(): FirebaseFirestore {
    val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(false)
      .setTimestampsInSnapshotsEnabled(true)
      .build()
    val db = FirebaseFirestore.getInstance()
    db.firestoreSettings = settings
    return db
  }

  private val remoteRepository = FirebaseSurveysRepository(fireStoreInstance())

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun should_store_human_surveys() {
    val humanA = createHuman("H-${UUID.randomUUID()}", Date(), null)
    val humanB = createHuman("H-${UUID.randomUUID()}", Date(), null)
    val humanC = createHuman("H-${UUID.randomUUID()}", Date(), null)
    remoteRepository.storeSurvey(humanA)
    remoteRepository.storeSurvey(humanB)
    remoteRepository.storeSurvey(humanC)
    Thread.sleep(5000)

    fireStoreInstance().collection(AppSettings.Constants.HUMAN_COLLECTION).document(humanA.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], humanA.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.HUMAN_COLLECTION).document(humanB.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], humanB.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.HUMAN_COLLECTION).document(humanC.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], humanC.id)
          }
          false -> fail()
        }
      }
  }

  @Test
  fun should_store_vector_batch_surveys() {
    val vectorBatchA = createVectorBatch("VB-${UUID.randomUUID()}", Date(), null)
    val vectorBatchB = createVectorBatch("VB-${UUID.randomUUID()}", Date(), null)
    val vectorBatchC = createVectorBatch("VB-${UUID.randomUUID()}", Date(), null)
    remoteRepository.storeSurvey(vectorBatchA)
    remoteRepository.storeSurvey(vectorBatchB)
    remoteRepository.storeSurvey(vectorBatchC)
    Thread.sleep(5000)

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_BATCH_COLLECTION).document(vectorBatchA.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], vectorBatchA.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_BATCH_COLLECTION).document(vectorBatchB.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], vectorBatchB.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_BATCH_COLLECTION).document(vectorBatchC.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], vectorBatchC.id)
          }
          false -> fail()
        }
      }
  }

  @Test
  fun should_store_vector_surveys() {
    val vectorBatchA = createVectorBatch("VB-${UUID.randomUUID()}", Date(), null)

    val vectorA = createVector("V-${UUID.randomUUID()}", vectorBatchA.id, null)
    val vectorB = createVector("V-${UUID.randomUUID()}", vectorBatchA.id, null)
    val vectorC = createVector("V-${UUID.randomUUID()}", vectorBatchA.id, null)
    remoteRepository.storeSurvey(vectorA)
    remoteRepository.storeSurvey(vectorB)
    remoteRepository.storeSurvey(vectorC)
    Thread.sleep(5000)

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_COLLECTION).document(vectorA.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], vectorA.id)
            assertEquals(doc["batchId"], vectorBatchA.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_COLLECTION).document(vectorB.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["id"], vectorB.id)
            assertEquals(doc["batchId"], vectorBatchA.id)
          }
          false -> fail()
        }
      }

    fireStoreInstance().collection(AppSettings.Constants.VECTOR_COLLECTION).document(vectorC.id).get()
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val doc = it.result
            assertEquals(doc["batchId"], vectorBatchA.id)
          }
          false -> fail()
        }
      }
  }

}
