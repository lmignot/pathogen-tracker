package eu.mignot.pathogentracker.addsurveys

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.widget.EditText
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchSurveyActivity
import org.hamcrest.Matchers.not
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
open class AddVectorBatchTest {

  @get:Rule
  val addVBRule =
    ActivityTestRule(AddVectorBatchSurveyActivity::class.java, false, false)

  @get:Rule val permRuleLocation: GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  private val surveyRepository:SurveyRepository = App.getLocalSurveysRepository()

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun has_a_default_id() {
    addVBRule.launchActivity(null)
    Thread.sleep(1000)

    // check a default id exists
    onView(withId(R.id.batchId)).check(matches(not(withText(""))))
  }

  @Test
  fun has_a_default_date() {
    addVBRule.launchActivity(null)
    Thread.sleep(1000)
    onView(withId(R.id.batchDate)).check(matches(not(withText(""))))
  }

  @Test
  fun can_get_location() {
    addVBRule.launchActivity(null)
    Thread.sleep(1000)
    // check it's empty
    onView(withId(R.id.batchLocation)).check(matches(withText("Tap to update location")))
    // ask for location
    onView(withId(R.id.batchLocation)).perform(click())
    // see what happens
    Thread.sleep(2000)
    onView(withId(R.id.batchLocation)).check(matches(not(withText(""))))
  }

  @Test
  fun fill_and_save() {
    addVBRule.launchActivity(null)
    Thread.sleep(500)
    // get the id to test saving
    val idField = addVBRule.activity.findViewById(R.id.batchId) as EditText
    val id = idField.text.toString()

    onView(withId(R.id.batchLocation)).perform(click())

    onView(withText(R.string.urban)).perform(click())

    // save it
    onView(withId(R.id.actionSave)).perform(click())

    Thread.sleep(1000)

    val survey: VectorBatch? = surveyRepository.getSurvey(VectorBatch(), id)
    assertNotNull(survey)
  }
}
