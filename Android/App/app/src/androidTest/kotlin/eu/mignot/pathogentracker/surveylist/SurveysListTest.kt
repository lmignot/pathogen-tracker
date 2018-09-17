package eu.mignot.pathogentracker.surveylist

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.TestCommon.clearRealm
import eu.mignot.pathogentracker.TestCommon.createHuman
import eu.mignot.pathogentracker.TestCommon.createVector
import eu.mignot.pathogentracker.TestCommon.createVectorBatch
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
@LargeTest
open class SurveysListTest {

  @get:Rule
  val surveysListRule =
    ActivityTestRule(SurveysActivity::class.java, false, false)

  private val cm = InstrumentationRegistry.getTargetContext()
    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private val wm = InstrumentationRegistry.getTargetContext()
    .getSystemService(Context.WIFI_SERVICE) as WifiManager

  private val localRepo = App.getLocalSurveysRepository()

  private val loginProvider = App.getLoginProvider()

  @Before
  fun startUp() {
    clearRealm()
  }

  @Test
  fun should_show_welcome_message_if_no_surveys() {
    // Given a user has opened the app
    launchSurveysActivity()
    Thread.sleep(1000)

    // When no surveys have been taken
    assertTrue(App.getLocalSurveysRepository().getSurveys(Human()).isEmpty())
    assertTrue(App.getLocalSurveysRepository().getSurveys(VectorBatch()).isEmpty())

    // Then they should be informed how to record surveys
    onView(withId(R.id.noData)).check(matches(isDisplayed()))
  }

  @Test
  fun should_not_show_welcome_message_if_has_surveys() {
    // Given a user has opened the app
    // When surveys have been taken
    addSomeSurveys()
    Thread.sleep(500)
    launchSurveysActivity()
    Thread.sleep(1000)

    // Then they should see surveys in the list
    onView(withId(R.id.noData)).check(matches(not(isDisplayed())))
  }

  @Test
  fun should_take_both_survey_types_if_configured() {
    // Given a user has opened the app
    resetSurveyTypes(SurveyType.PATIENT)

    // And they have set both survey types
    AppPreferencesProvider.setSecondarySurveyActivity(SurveyType.VECTOR)
    launchSurveysActivity()
    Thread.sleep(1000)

    // When they select the add survey button
    onView(withId(R.id.fabAddSurvey)).perform(click())

    // Then the app should allow them to choose to create a patient or mosquito survey
    onView(withText(R.string.survey_type_human)).inRoot(isDialog()).check(matches(isDisplayed()))
    onView(withText(R.string.survey_type_vector)).inRoot(isDialog()).check(matches(isDisplayed()))
  }

  @Test
  fun should_take_patient_survey_directly_if_configured() {
    // Given a user has opened the app
    // And they have selected patient surveys as their only survey type
    resetSurveyTypes(SurveyType.PATIENT)
    launchSurveysActivity()
    Thread.sleep(1000)

    // When they select the add survey button
    onView(withId(R.id.fabAddSurvey)).perform(click())
    Thread.sleep(500)

    // Then the app should allow them to choose to create a patient or mosquito survey
    onView(withText(R.string.title_human)).check(matches(isDisplayed()))
  }

  @Test
  fun should_take_mosquito_survey_directly_if_configured() {
    // Given a user has opened the app
    // And they have selected mosquito surveys as their only survey type
    resetSurveyTypes(SurveyType.VECTOR)
    launchSurveysActivity()
    Thread.sleep(1000)

    // When they select the add survey button
    onView(withId(R.id.fabAddSurvey)).perform(click())
    Thread.sleep(500)

    // Then the app should allow them to choose to create a patient or mosquito survey
    onView(withText(R.string.title_vector_batch)).check(matches(isDisplayed()))
  }

  @Test
  fun should_allow_patient_survey_review() {
    clearRealm()

    // Given a user has saved at least one patient survey
    val h1 = createHuman("H-${UUID.randomUUID()}", Date(), Date())
    h1.save()
    Thread.sleep(500)
    launchSurveysActivity()
    Thread.sleep(1000)

    // When they select a patient survey from the survey list
    onView(withText(h1.id)).perform(click())

    // Then the app should present the data for the selected patient survey
    onView(withId(R.id.humanSurveyDetailId)).check(matches(withText(h1.id)))
  }

  @Test
  fun should_allow_vector_batch_survey_review() {
    clearRealm()

    // Given a user has saved at least one vector batch survey
    val vb1 = createVectorBatch("VB-${UUID.randomUUID()}", Date(), Date())
    vb1.save()
    Thread.sleep(500)
    launchSurveysActivity()
    Thread.sleep(1000)

    // When they select a vector batch survey from the survey list
    onView(withText(vb1.id)).perform(click())

    // Then the app should present the data for the selected vector batch survey
    onView(withId(R.id.vectorBatchSurveyDetailId)).check(matches(withText(vb1.id)))
  }

  @Test
  @Ignore
  fun should_allow_vector_detail_survey_review() {
    clearRealm()

    // Given a user has saved at least one vector batch survey
    // And at least one vector detail survey in the batch

    val vb1 = createVectorBatch("VB-${UUID.randomUUID()}", Date(), Date())
    val v1 = createVector("V-${UUID.randomUUID()}", vb1.id, Date())
    v1.sequence = 1
    vb1.save()
    v1.save()
    Thread.sleep(500)
    launchSurveysActivity()
    Thread.sleep(500)

    // And the mosquito batch survey is being reviewed
    onView(withText(vb1.id)).perform(click())
    Thread.sleep(500)

    // When the user selects a mosquito detail survey from the list
    val v1IdString = InstrumentationRegistry.getTargetContext()
        .getString(R.string.survey_vector_item_id, v1.id, String.format("%03d",v1.sequence))
    onView(withText(v1IdString)).check(matches(isDisplayed()))

    // Then the app should present the data for the selected mosquito detail survey
    onView(withId(R.id.vectorBatchSurveyDetailId)).check(matches(withText(v1IdString)))
  }

  private fun resetSurveyTypes(primary: SurveyType) {
    AppPreferencesProvider.setPrimarySurveyActivity(primary)
    AppPreferencesProvider.setSecondarySurveyActivity(SurveyType.NONE)
  }

  private fun addSomeSurveys() {
    createHuman("H-${UUID.randomUUID()}", Date(), null).save()
    createHuman("H-${UUID.randomUUID()}", Date(), null).save()
    createHuman("H-${UUID.randomUUID()}", Date(), null).save()
    createVectorBatch("VB-${UUID.randomUUID()}", Date(), null).save()
    createVectorBatch("VB-${UUID.randomUUID()}", Date(), null).save()
    createVectorBatch("VB-${UUID.randomUUID()}", Date(), null).save()
  }

  private fun launchSurveysActivity() {
    val intent = Intent()
    intent.putExtra(AppSettings.Constants.SHOULD_AUTHENTICATE, false)
    surveysListRule.launchActivity(intent)
  }

}
