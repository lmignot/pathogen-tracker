package eu.mignot.pathogentracker.firstTimeUser

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.R.id.*
import eu.mignot.pathogentracker.TestCommon.signIn
import eu.mignot.pathogentracker.TestCommon.signOut
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.launcher.AppLauncher
import eu.mignot.pathogentracker.onboarding.OnBoarding
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
open class FirstTimeUserTest {

  private val loginProvider = App.getLoginProvider()
  private val prefsProvider = AppPreferencesProvider

  @get:Rule
  val launcherRule =
    ActivityTestRule(AppLauncher::class.java, false, false)

  @get:Rule
  val onBoardingRule =
    ActivityTestRule(OnBoarding::class.java, true, false)

  @get:Rule val permRuleLocation:GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)
  @get:Rule val permCamera:GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.CAMERA)
  @get:Rule val permStorage:GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

  private fun afterEach() {
    launcherRule.activity?.finish()
    onBoardingRule.activity?.finish()
  }

  @Test
  fun users_should_first_sign_in_to_the_app() {
    signOut()
    prefsProvider.setDidCompleteOnBoarding(false)
    launcherRule.launchActivity(null)

    // Given the app is installed
    // And the user has not used the app
    assertFalse(loginProvider.hasUser())

    // When the app is running
    // Then they must be prompted to sign in
    Thread.sleep(3000)

    onView(withText("Sign in")).check(matches(isDisplayed()))

    afterEach()
  }

  @Test
  fun allow_a_user_to_sign_out() {
    // Given i have signed in to the app
    // And i want to sign out of the app
    signIn()
    Thread.sleep(2000)

    // When I choose to sign out
    signOut()

    // Then the app should sign me out
    assertFalse(loginProvider.hasUser())

    afterEach()
  }

  @Test
  fun first_time_user_can_select_primary_survey_as_patient() {
    // Given a user has signed in to the app
    signIn()
    Thread.sleep(1000)
    onBoardingRule.launchActivity(null)

    // When they use the app for the first time
    assertFalse(prefsProvider.getDidCompleteOnBoarding())
    prefsProvider.setPrimarySurveyActivity(SurveyType.NONE)
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
    // Then they should be able to select their primary surveytype
    onView(withId(primaryActivityPatient)).perform(click())
    Thread.sleep(2000)
    assertTrue(prefsProvider.getPrimarySurveyActivity() == SurveyType.PATIENT)
    afterEach()
  }

  @Test
  fun first_time_user_can_select_primary_survey_as_mosquito() {
    // Given a user has signed in to the app
    Thread.sleep(1000)
    onBoardingRule.launchActivity(null)
    // When they use the app for the first time
    prefsProvider.setPrimarySurveyActivity(SurveyType.NONE)
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
    assertFalse(prefsProvider.getDidCompleteOnBoarding())
    // Then they should be able to select their primary surveytype
    onView(withId(primaryActivityVector)).perform(click())
    Thread.sleep(2000)
    assertTrue(prefsProvider.getPrimarySurveyActivity() == SurveyType.VECTOR)

    afterEach()
  }

  @Test
  fun first_time_user_can_select_secondary_survey_as_mosquito() {
    // Given a user has signed in to the app
    Thread.sleep(1000)
    onBoardingRule.launchActivity(null)
    // When they use the app for the first time
    assertFalse(prefsProvider.getDidCompleteOnBoarding())
    // Then they should be able to select their secondary surveytype
    onView(withId(primaryActivityPatient)).perform(click())
    Thread.sleep(2000)
    onView(withId(secondaryActivityTrue)).perform(click())
    assertTrue(prefsProvider.getSecondarySurveyActivity() == SurveyType.VECTOR)

    afterEach()
  }

  @Test
  fun first_time_user_can_select_secondary_survey_as_patient() {
    // Given a user has signed in to the app
    Thread.sleep(1000)
    onBoardingRule.launchActivity(null)
    // When they use the app for the first time
    assertFalse(prefsProvider.getDidCompleteOnBoarding())
    // Then they should be able to select their secondary surveytype
    onView(withId(primaryActivityVector)).perform(click())
    Thread.sleep(2000)
    onView(withId(secondaryActivityTrue)).perform(click())
    assertTrue(prefsProvider.getSecondarySurveyActivity() == SurveyType.PATIENT)

    afterEach()
  }

  @Test
  fun first_time_user_can_select_secondary_survey_as_none() {
    // Given a user has signed in to the app
    Thread.sleep(1000)
    onBoardingRule.launchActivity(null)
    // When they use the app for the first time
    assertFalse(prefsProvider.getDidCompleteOnBoarding())
    // Then they should be able to select their secondary surveytype
    onView(withId(primaryActivityVector)).perform(click())
    Thread.sleep(2000)
    onView(withId(secondaryActivityFalse)).perform(click())
    assertTrue(prefsProvider.getSecondarySurveyActivity() == SurveyType.NONE)

    afterEach()
  }

  @Test
  fun first_time_user_encryption_and_complete() {
    // Given a user has signed in to the app
    signIn()
    Thread.sleep(1000)

    // When they use the app for the first time
    prefsProvider.setPrimarySurveyActivity(SurveyType.NONE)
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
    prefsProvider.setDidCompleteOnBoarding(false)
    onBoardingRule.launchActivity(null)
    Thread.sleep(1000)
    onView(withId(primaryActivityVector)).perform(click())
    Thread.sleep(2000)
    onView(withId(secondaryActivityTrue)).perform(click())
    Thread.sleep(2000)

    // And they should be prompted to encrypt their device
    onView(withText(R.string.encrpytion_rationale))
      .check(matches(isDisplayed()))
    Thread.sleep(2000)

    // And then on boarding should be complete
    onView(withId(finishOnboarding)).perform(click())
    Thread.sleep(2000)
    assertTrue(prefsProvider.getDidCompleteOnBoarding())

    afterEach()
  }

}
