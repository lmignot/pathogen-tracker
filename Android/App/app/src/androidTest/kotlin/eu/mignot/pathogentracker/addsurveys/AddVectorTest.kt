package eu.mignot.pathogentracker.addsurveys

import android.content.Intent
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorSurveyActivity
import eu.mignot.pathogentracker.util.AppSettings
import org.hamcrest.Matchers.anything
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
open class AddVectorTest {

  @get:Rule
  val addVBRule =
    ActivityTestRule(AddVectorSurveyActivity::class.java, false, false)

  @get:Rule val permRuleLocation: GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  private val surveyRepository:SurveyRepository = App.getLocalSurveysRepository()

  @Before
  fun startUp() {
    clearRealm()
  }

  private fun launchVectorActivity(vectorId: String, batchId: String) {
    val intent = Intent()
    intent.putExtra(
      AppSettings.Constants.VECTOR_ID_KEY, vectorId
    )
    intent.putExtra(
      AppSettings.Constants.BATCH_ID_KEY, batchId
    )
    addVBRule.launchActivity(intent)
  }

  @Test
  fun fill_and_save() {
    val id = "V-${UUID.randomUUID()}"
    launchVectorActivity(id, "V-${UUID.randomUUID()}")
    Thread.sleep(1000)

    onView(withId(R.id.vectorSpecies)).perform(click())
    onData(anything()).atPosition(3).perform(click())

    onView(withId(R.id.actionSave)).perform(click())

    Thread.sleep(1000)

    val survey: Vector? = surveyRepository.getSurvey(Vector(), id)
    Assert.assertNotNull(survey)
  }
}
