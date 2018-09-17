package eu.mignot.pathogentracker.repository

import android.support.test.runner.AndroidJUnit4
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.TestCommon.createHuman
import eu.mignot.pathogentracker.TestCommon.createVector
import eu.mignot.pathogentracker.TestCommon.createVectorBatch
import eu.mignot.pathogentracker.data.models.database.Human
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
    createHuman(surveyId, collectedDate, Date()).save()
    val savedSurvey = localRepository.getSurvey(Human(), surveyId)
    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(collectedDate, savedSurvey.collectedOn)
  }

  @Test
  fun should_get_a_Human_survey() {
    val surveyId = "TEST_HUMAN_ID_2"
    val collected = Date()
    createHuman(surveyId, collected, Date()).save()

    val stored = localRepository.getSurvey(Human(), surveyId)

    assertNotNull(stored)
    assertEquals(stored!!.id, surveyId)
    assertEquals(stored.collectedOn, collected)
  }

  @Test
  fun should_store_a_VectorBatch_survey() {
    val surveyId = "TEST_VB_ID_1"
    val collected = Date()
    createVectorBatch(surveyId, collected, Date()).save()

    val savedSurvey = localRepository.getSurvey(VectorBatch(), surveyId)

    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(collected, savedSurvey.collectedOn)
  }

  @Test
  fun should_get_a_VectorBatch_survey() {
    val surveyId = "TEST_VB_ID_2"
    val collectedDate = Date()
    createVectorBatch(surveyId, collectedDate, Date()).save()

    val stored = localRepository.getSurvey(VectorBatch(), surveyId)

    assertNotNull(stored)
    assertEquals(stored!!.id, surveyId)
  }

  @Test
  fun should_store_a_Vector_survey() {
    val surveyId = "TEST_V_ID_1"
    val batchId = "TEST_VB_ID_1"
    createVector(surveyId, batchId, Date()).save()

    val savedSurvey = localRepository.getSurvey(Vector(), surveyId)

    assertNotNull(savedSurvey)
    assertEquals(surveyId, savedSurvey!!.id)
    assertEquals(batchId, savedSurvey.batchId)
  }

  @Test
  fun should_get_a_Vector_survey() {
    val surveyId = "TEST_V_ID_2"
    val batchId = "TEST_VB_ID_2"
    createVector(surveyId, batchId, Date()).save()

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
    createVectorBatch(batchId2, collectedDate, Date()).save()
    createVectorBatch(batchId3, collectedDate, Date()).save()
    createVector(surveyId2, batchId2, Date()).save()
    createVector(surveyId2, batchId2, Date()).save()
    createVector(surveyId3, batchId2, Date()).save()
    createVector(surveyId4, batchId3, Date()).save()

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
    createHuman(human1, collectedDate, null).save()
    createHuman(human2, collectedDate, Date()).save()
    createVectorBatch(vectorBatch2, collectedDate, null).save()
    createVectorBatch(vectorBatch3, collectedDate, Date()).save()
    createVector(vector2, vectorBatch2, null).save()
    createVector(vector3, vectorBatch3, null).save()
    createVector(vector4, vectorBatch2, Date()).save()
    val h2ul = localRepository.getSurveysToSync(Human())
    val v2ul = localRepository.getSurveysToSync(Vector())
    val vb2ul = localRepository.getSurveysToSync(VectorBatch())
    assertEquals(1,h2ul.size)
    assertEquals(2,v2ul.size)
    assertEquals(1,vb2ul.size)
  }

}
