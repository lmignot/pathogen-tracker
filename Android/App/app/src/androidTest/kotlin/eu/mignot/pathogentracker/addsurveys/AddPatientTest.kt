package eu.mignot.pathogentracker.addsurveys

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanSurveyActivity
import org.hamcrest.Matchers.*
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
open class AddPatientTest {

  @get:Rule
  val addPatientRule =
    ActivityTestRule(AddHumanSurveyActivity::class.java, false, false)

  @get:Rule val permRuleLocation: GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  private val surveyRepository:SurveyRepository = App.getLocalSurveysRepository()

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun has_a_default_id() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)

    // check a default id exists
    onView(withId(R.id.patientId)).check(matches(not(withText(""))))
  }

  @Test
  fun has_a_default_date() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    onView(withId(R.id.dateCollected)).check(matches(not(withText(""))))
  }

  @Test
  fun can_get_location() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // check it's empty
    onView(withId(R.id.surveyLocation)).check(matches(withText("Tap to update location")))
    // ask for location
    onView(withId(R.id.surveyLocation)).perform(click())
    // see what happens
    Thread.sleep(2000)
    onView(withId(R.id.surveyLocation)).check(matches(not(withText(""))))
  }

  @Test
  fun can_navigate_between_pages() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // navigate to next screen
    onView(withText("NEXT")).perform(click())
    // test it worked
    onView(withId(R.id.surveySampleTypes)).check(matches(isDisplayed()))
    // navigate to next screen
    onView(withText("NEXT")).perform(click())
    // test it worked
    onView(withId(R.id.surveyAddCurrentDisease)).check(matches(isDisplayed()))
    // navigate back
    onView(withText("BACK")).perform(click())
    // test it worked
    onView(withId(R.id.surveySampleTypes)).check(matches(isDisplayed()))
    // navigate back
    onView(withText("BACK")).perform(click())
    // test it worked
    onView(withId(R.id.surveyLocation)).check(matches(isDisplayed()))

  }

  @Test
  fun set_sample_types() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // navigate to next screen
    onView(withText("NEXT")).perform(click())

    //
    val spinner1 = onView(
      allOf(
        isDescendantOfA(withId(R.id.surveySampleTypes)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    )
    spinner1.perform(click())
    onData(anything()).atPosition(1).perform(click())
    spinner1.check(matches(withSpinnerText(containsString("Blood"))))
  }

  @Test
  fun set_travel_history() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // navigate to next screen
    onView(withText("NEXT")).perform(click())

    val spinner1 = onView(
      allOf(
        isDescendantOfA(withId(R.id.surveyTravelHistory)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    )
    spinner1.perform(click())
    onData(anything()).atPosition(8).perform(click())
    spinner1.check(matches(withSpinnerText(containsString("Antarctica"))))
  }

  @Test
  fun set_infection_history() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // navigate to next screen
    onView(withText("NEXT")).perform(click())

    val spinner1 = onView(
      allOf(
        isDescendantOfA(withId(R.id.surveyInfectionHistory)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    )
    spinner1.perform(click())
    onData(anything()).atPosition(3).perform(click())
    spinner1.check(matches(withSpinnerText(containsString("Malaria"))))
  }

  @Test
  fun set_current_disease() {
    addPatientRule.launchActivity(null)
    Thread.sleep(1000)
    // navigate to next screen
    onView(withText("NEXT")).perform(click())
    Thread.sleep(500)
    onView(withText("NEXT")).perform(click())

    onView(
      withId(R.id.surveyAddCurrentDisease)
    ).perform(click())

    onView(allOf(
      withParent(instanceOf(LinearLayout::class.java)),
      instanceOf(Spinner::class.java)
    )).inRoot(isDialog()).perform(click())

    onData(anything()).inRoot(isPlatformPopup()).atPosition(1).perform(click())

    onView(withText("Skin rash")).inRoot(isDialog()).perform(click())

    onView(allOf(
      instanceOf(Button::class.java),
      withText("Save")
    )).inRoot(isDialog()).perform(click())
  }

  @Test
  fun fill_and_save() {
    addPatientRule.launchActivity(null)
    Thread.sleep(500)
    // get the id to test saving
    val idField = addPatientRule.activity.findViewById(R.id.patientId) as EditText
    val id = idField.text.toString()

    onView(withId(R.id.surveyLocation)).perform(click())
    // navigate to next screen
    onView(withText("NEXT")).perform(click())

    onView(
      allOf(
        isDescendantOfA(withId(R.id.surveySampleTypes)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    ).perform(click())
    onData(anything()).atPosition(1).perform(click())

    onView(
      allOf(
        isDescendantOfA(withId(R.id.surveyTravelHistory)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    ).perform(click())
    onData(anything()).atPosition(8).perform(click())

    onView(
      allOf(
        isDescendantOfA(withId(R.id.surveyInfectionHistory)),
        withParent(instanceOf(LinearLayout::class.java)),
        instanceOf(Spinner::class.java)
      )
    ).perform(click())
    onData(anything()).atPosition(3).perform(click())

    // navigate to next screen
    onView(withText("NEXT")).perform(click())

    // current diseases dialog
    onView(
      withId(R.id.surveyAddCurrentDisease)
    ).perform(click())

    onView(allOf(
      withParent(instanceOf(LinearLayout::class.java)),
      instanceOf(Spinner::class.java)
    )).inRoot(isDialog()).perform(click())

    onData(anything()).inRoot(isPlatformPopup()).atPosition(1).perform(click())

    onView(withText("Skin rash")).inRoot(isDialog()).perform(click())

    onView(allOf(
      instanceOf(Button::class.java),
      withText("Save")
    )).inRoot(isDialog()).perform(click())

    // flag the thing
    onView(withId(R.id.surveyIsFlagged)).perform(click())

    // save it
    onView(withId(R.id.actionSave)).perform(click())

    Thread.sleep(1000)

    val survey: Human? = surveyRepository.getSurvey(Human(), id)
    assertNotNull(survey)
  }
}
