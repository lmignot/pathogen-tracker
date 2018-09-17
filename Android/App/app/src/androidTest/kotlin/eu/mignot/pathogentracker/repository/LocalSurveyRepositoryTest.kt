package eu.mignot.pathogentracker.repository

import android.support.test.runner.AndroidJUnit4
import com.vicpin.krealmextensions.deleteAll
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.Location
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import io.realm.RealmConfiguration
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class LocalSurveyRepositoryTest {

  companion object {
    const val TEST_LAT = 37.377166
    const val TEST_LONG = -122.086966
    const val TEST_ACC= 2.0F
  }

  private val realmConfig = RealmConfiguration
    .Builder()
    .inMemory()
    .name("LocalRepoTest")
    .schemaVersion(98)
    .deleteRealmIfMigrationNeeded()
    .build()

  private val localRepository = RealmSurveysRepository(realmConfig)

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun should_store_a_Human_survey() {
    val surveyId = "TEST_HUMAN_ID_1"
    val collectedDate = Date()
    createAndStoreHuman(surveyId, collectedDate, Date())
    val savedSurvey = localRepository.getSurvey(Human(), surveyId)
    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(collectedDate, savedSurvey.collectedOn)
  }

  @Test
  fun should_get_a_Human_survey() {
    val surveyId = "TEST_HUMAN_ID_2"
    val collected = Date()
    createAndStoreHuman(surveyId, collected, Date())

    val stored = localRepository.getSurvey(Human(), surveyId)

    assertNotNull(stored)
    assertEquals(stored!!.id, surveyId)
    assertEquals(stored.collectedOn, collected)
  }

  @Test
  fun should_store_a_VectorBatch_survey() {
    val surveyId = "TEST_VB_ID_1"
    val collected = Date()
    createAndStoreVectorBatch(surveyId, collected, Date())

    val savedSurvey = localRepository.getSurvey(VectorBatch(), surveyId)

    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(collected, savedSurvey.collectedOn)
  }

  @Test
  fun should_get_a_VectorBatch_survey() {
    val surveyId = "TEST_VB_ID_2"
    val collectedDate = Date()
    createAndStoreVectorBatch(surveyId, collectedDate, Date())

    val stored = localRepository.getSurvey(VectorBatch(), surveyId)

    assertNotNull(stored)
    assertEquals(stored!!.id, surveyId)
  }

  @Test
  fun should_store_a_Vector_survey() {
    val surveyId = "TEST_V_ID_1"
    val batchId = "TEST_VB_ID_1"
    createAndStoreVector(surveyId, batchId, Date())

    val savedSurvey = localRepository.getSurvey(Vector(), surveyId)

    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(batchId, savedSurvey.batchId)
  }

  @Test
  fun should_get_a_Vector_survey() {
    val surveyId = "TEST_V_ID_2"
    val batchId = "TEST_VB_ID_2"
    createAndStoreVector(surveyId, batchId, Date())

    val stored = localRepository.getSurvey(Vector(), surveyId)

    assertNotNull(stored)
    assertEquals(stored!!.id, surveyId)
  }

  @Test
  fun should_get_vector_surveys_for_vector_batch() {
    val surveyId2 = "TEST_V_ID_3"
    val surveyId3 = "TEST_V_ID_4"
    val surveyId4 = "TEST_V_ID_5"
    val batchId2 = "TEST_VB_ID_3"
    val batchId3 = "TEST_VB_ID_4"
    val collectedDate = Date()
    createAndStoreVectorBatch(batchId2, collectedDate, Date())
    createAndStoreVectorBatch(batchId3, collectedDate, Date())
    createAndStoreVector(surveyId2, batchId2, Date())
    createAndStoreVector(surveyId2, batchId2, Date())
    createAndStoreVector(surveyId3, batchId2, Date())
    createAndStoreVector(surveyId4, batchId3, Date())

    val surveysForBatch2 = localRepository.getSurveysFor(Vector(), batchId2)
    val surveysForBatch3 = localRepository.getSurveysFor(Vector(), batchId3)

    assertFalse(surveysForBatch2.isEmpty())
    assertFalse(surveysForBatch3.isEmpty())
    assertEquals(2, surveysForBatch2.size)
    assertEquals(1, surveysForBatch3.size)
  }

  @Test
  fun should_get_surveys_to_sync() {
    val human1 = "TEST_HUMAN_ID_5"
    val human2 = "TEST_HUMAN_ID_6"
    val vector2 = "TEST_V_ID_6"
    val vector3 = "TEST_V_ID_7"
    val vector4 = "TEST_V_ID_8"
    val vectorBatch2 = "TEST_VB_ID_7"
    val vectorBatch3 = "TEST_VB_ID_8"
    val collectedDate = Date()
    createAndStoreHuman(human1, collectedDate, null)
    createAndStoreHuman(human2, collectedDate, Date())
    createAndStoreVectorBatch(vectorBatch2, collectedDate, null)
    createAndStoreVectorBatch(vectorBatch3, collectedDate, Date())
    createAndStoreVector(vector2, vectorBatch2, null)
    createAndStoreVector(vector3, vectorBatch3, null)
    createAndStoreVector(vector4, vectorBatch2, Date())
    val h2ul = localRepository.getSurveysToSync(Human())
    val v2ul = localRepository.getSurveysToSync(Vector())
    val vb2ul = localRepository.getSurveysToSync(VectorBatch())
    assertEquals(1,h2ul.size)
    assertEquals(2,v2ul.size)
    assertEquals(1,vb2ul.size)
  }

  private fun createAndStoreHuman(id: String, date: Date, uploadedAt: Date?) {
    val location = with(Location()) {
      this.latitude = TEST_LAT
      this.longitude = TEST_LONG
      this.accuracy = TEST_ACC
      this
    }
    val survey = with(Human()) {
      this.id = id
      this.collectedOn = date
      this.locationCollected = location
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
    localRepository.storeSurvey(survey)
  }

  private fun createAndStoreVectorBatch(id: String, date: Date, uploadedAt: Date?) {
    val location = with(Location()) {
      this.latitude = TEST_LAT
      this.longitude = TEST_LONG
      this.accuracy = TEST_ACC
      this
    }
    val survey = with(VectorBatch()) {
      this.id = id
      this.collectedOn = date
      this.locationCollected = location
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
    localRepository.storeSurvey(survey)
  }

  private fun createAndStoreVector(id: String, batchId: String, uploadedAt: Date?) {
    val survey = with(Vector()) {
      this.id = id
      this.batchId = batchId
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
    localRepository.storeSurvey(survey)
  }

  private fun clearRealm() {
    Location().deleteAll()
    Vector().deleteAll()
    VectorBatch().deleteAll()
    Human().deleteAll()
  }
}
