package eu.mignot.pathogentracker.surveylist

import android.support.test.runner.AndroidJUnit4
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.data.models.database.*
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import eu.mignot.pathogentracker.surveys.surveys.SurveysViewModel
import io.realm.RealmConfiguration
import org.jetbrains.anko.AnkoLogger
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TestSurveysViewModel: AnkoLogger {

  companion object {
    const val TEST_HUMAN_A_ID = "H-TEST_HUMAN_A"
    const val TEST_HUMAN_B_ID = "H-TEST_HUMAN_B"
    const val TEST_HUMAN_C_ID = "H-TEST_HUMAN_C"
    const val TEST_VB_A_ID = "VB-TEST_VB_A"
    const val TEST_VB_B_ID = "VB-TEST_VB_B"
    const val TEST_VB_C_ID = "VB-TEST_VB_C"
    const val TEST_LAT = 37.377166
    const val TEST_LONG = -122.086966
    const val TEST_ACC= 2.0F
  }

  private val collectionDate = Date()

  private val realmConfig = RealmConfiguration
    .Builder()
    .inMemory()
    .name("Surveys VM Test A")
    .schemaVersion(98)
    .deleteRealmIfMigrationNeeded()
    .build()

  private val repository = RealmSurveysRepository(realmConfig)

  private val vm = SurveysViewModel(repository)

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun get_surveys_returns_surveys_if_any_exist() {
    addSomeSurveys()
    val surveys = vm.getSurveys()
    assertEquals(6, surveys.size)
    assertFalse(surveys.isEmpty())
  }

  @Test
  fun get_surveys_returns_no_surveys_if_none_exist() {
    clearRealm()
    val surveys = vm.getSurveys()
    assertEquals(0, surveys.size)
    assertTrue(surveys.isEmpty())
  }

  private fun addSomeSurveys() {
    val location = with(Location()) {
      this.latitude = TEST_LAT
      this.longitude = TEST_LONG
      this.accuracy = TEST_ACC
      this
    }
    with(Human()) {
      this.id = TEST_HUMAN_A_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
    with(Human()) {
      this.id = TEST_HUMAN_B_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
    with(Human()) {
      this.id = TEST_HUMAN_C_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
    with(VectorBatch()) {
      this.id = TEST_VB_A_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
    with(VectorBatch()) {
      this.id = TEST_VB_B_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
    with(VectorBatch()) {
      this.id = TEST_VB_C_ID
      this.collectedOn = collectionDate
      this.locationCollected = location
      this
    }.save()
  }

  private fun clearRealm() {
    Location().deleteAll()
    Photo().deleteAll()
    Vector().deleteAll()
    VectorBatch().deleteAll()
    Human().deleteAll()
  }

}
