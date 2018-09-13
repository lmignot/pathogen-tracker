package eu.mignot.pathogentracker.launcher

import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppOnboardingTest {
/*
  companion object {
      const val USERNAME = "lmignot@gmail.com"
      const val PASSWORD = "4cmtux0k7"
  }

  @Rule
  var mActivityTestRule = ActivityTestRule(AppLauncher::class.java)

  @Test
  fun appOnboardingTest() {
    try {
      Thread.sleep(3598877)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textInputEditText2 = onView(
      allOf<View>(
        withId(R.id.email), withText(""),
        childAtPosition(
          childAtPosition(
            withId(R.id.email_layout),
            0
          ),
          0
        )
      )
    )
    textInputEditText2.perform(scrollTo(), replaceText("lmignot@gmail.com"))

    val textInputEditText3 = onView(
      allOf<View>(
        withId(R.id.email), withText("lmignot@gmail.com"),
        childAtPosition(
          childAtPosition(
            withId(R.id.email_layout),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textInputEditText3.perform(closeSoftKeyboard())

    val appCompatButton = onView(
      allOf<View>(
        withId(R.id.button_next), withText("Next"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`<String>("android.widget.ScrollView")),
            0
          ),
          1
        )
      )
    )
    appCompatButton.perform(scrollTo(), click())

    try {
      Thread.sleep(3548748)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textInputEditText4 = onView(
      allOf<View>(
        withId(R.id.password),
        childAtPosition(
          childAtPosition(
            withId(R.id.password_layout),
            0
          ),
          0
        )
      )
    )
    textInputEditText4.perform(scrollTo(), replaceText("4cmtux0k7"), closeSoftKeyboard())

    val appCompatButton2 = onView(
      allOf<View>(
        withId(R.id.button_done), withText("Sign in"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`<String>("android.widget.ScrollView")),
            0
          ),
          4
        )
      )
    )
    appCompatButton2.perform(scrollTo(), click())

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3569763)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView = onView(
      allOf<View>(
        withId(R.id.questionTextPrimary),
        withText("Is your primary activity collecting Patient or Mosquito data?"),
        childAtPosition(
          childAtPosition(
            withId(R.id.fragmentContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView.check(matches(withText("Is your primary activity collecting Patient or Mosquito data?")))

    val button = onView(
      allOf<View>(
        withId(R.id.primaryActivityPatient),
        childAtPosition(
          childAtPosition(
            IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    button.check(matches(isDisplayed()))

    val button2 = onView(
      allOf<View>(
        withId(R.id.primaryActivityVector),
        isDisplayed()
      )
    )
    button2.check(matches(isDisplayed()))

    val button3 = onView(
      allOf<View>(
        withId(R.id.primaryActivityVector),
        isDisplayed()
      )
    )
    button3.check(matches(isDisplayed()))

    val appCompatButton3 = onView(
      allOf<View>(
        withId(R.id.primaryActivityPatient), withText("Patient"),
        isDisplayed()
      )
    )
    appCompatButton3.perform(click())

    val textView2 = onView(
      allOf<View>(
        withId(R.id.questionTextSecondary), withText("Will you also be collecting Mosquito data?"),
        childAtPosition(
          childAtPosition(
            withId(R.id.fragmentContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView2.check(matches(withText("Will you also be collecting Mosquito data?")))

    val textView3 = onView(
      allOf<View>(
        withId(R.id.questionTextSecondary), withText("Will you also be collecting Mosquito data?"),
        childAtPosition(
          childAtPosition(
            withId(R.id.fragmentContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView3.check(matches(withText("Will you also be collecting Mosquito data?")))

    val appCompatButton4 = onView(
      allOf<View>(
        withId(R.id.secondaryActivityTrue), withText("Yes"),
        isDisplayed()
      )
    )
    appCompatButton4.perform(click())

    val textView4 = onView(
      allOf<View>(
        withId(R.id.questionText), withText("This app needs permission to access your location"),
        isDisplayed()
      )
    )
    textView4.check(matches(withText("This app needs permission to access your location")))

    val appCompatButton5 = onView(
      allOf<View>(
        withId(R.id.givePermission), withText("Give permission"),
        isDisplayed()
      )
    )
    appCompatButton5.perform(click())

    try {
      Thread.sleep(3524058)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView5 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.permission_message),
        withText("Allow PathogenTracker to access this device's location?"),
        childAtPosition(
          allOf(
            withId(com.android.packageinstaller.R.id.perm_desc_root),
            childAtPosition(
              withId(com.android.packageinstaller.R.id.desc_container),
              0
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView5.check(matches(withText("Allow PathogenTracker to access this device's location?")))

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3524073)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView6 = onView(
      allOf<View>(
        withId(R.id.questionText),
        withText("This app needs permission to access the Camera and Device Storage"),
        childAtPosition(
          childAtPosition(
            withId(R.id.fragmentContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView6.check(matches(withText("This app needs permission to access the Camera and Device Storage")))

    val appCompatButton6 = onView(
      allOf<View>(
        withId(R.id.givePermission), withText("Give permission"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`<String>("android.widget.RelativeLayout")),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatButton6.perform(click())

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3588330)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView7 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.current_page_text), withText("1 of 2"),
        childAtPosition(
          childAtPosition(
            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView7.check(matches(withText("1 of 2")))

    val textView8 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.permission_message),
        withText("Allow PathogenTracker to take pictures and record video?"),
        childAtPosition(
          allOf(
            withId(com.android.packageinstaller.R.id.perm_desc_root),
            childAtPosition(
              withId(com.android.packageinstaller.R.id.desc_container),
              0
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView8.check(matches(withText("Allow PathogenTracker to take pictures and record video?")))

    val textView9 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.permission_message),
        withText("Allow PathogenTracker to take pictures and record video?"),
        childAtPosition(
          allOf(
            withId(com.android.packageinstaller.R.id.perm_desc_root),
            childAtPosition(
              withId(com.android.packageinstaller.R.id.desc_container),
              0
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView9.check(matches(withText("Allow PathogenTracker to take pictures and record video?")))

    val textView10 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.current_page_text), withText("2 of 2"),
        childAtPosition(
          childAtPosition(
            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView10.check(matches(withText("2 of 2")))

    val textView11 = onView(
      allOf(
        withId(com.android.packageinstaller.R.id.permission_message),
        withText("Allow PathogenTracker to access photos, media, and files on your device?"),
        childAtPosition(
          allOf(
            withId(com.android.packageinstaller.R.id.perm_desc_root),
            childAtPosition(
              withId(com.android.packageinstaller.R.id.desc_container),
              0
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView11.check(matches(withText("Allow PathogenTracker to access photos, media, and files on your device?")))

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3588342)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView12 = onView(
      allOf<View>(
        withId(R.id.questionText),
        withText("Please ensure your \nsmartphone device is encrypted if mandated by your organization"),
        childAtPosition(
          childAtPosition(
            withId(R.id.fragmentContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView12.check(matches(withText("Please ensure your  smartphone device is encrypted if mandated by your organization")))

    val appCompatButton7 = onView(
      allOf<View>(
        withId(R.id.finishOnboarding), withText("Done"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`<String>("android.widget.RelativeLayout")),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatButton7.perform(click())

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(3580765)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }

    val textView13 = onView(
      allOf<View>(
        withText("Surveys"),
        childAtPosition(
          allOf<View>(
            withId(R.id.toolbar),
            childAtPosition(
              withId(R.id.surveyListRoot),
              0
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView13.check(matches(withText("Surveys")))

    val textView14 = onView(
      allOf<View>(
        withId(R.id.userNameTextView), withText("Welcome stranger"),
        childAtPosition(
          allOf<View>(
            withId(R.id.noData),
            childAtPosition(
              withId(R.id.coordinatorLayout),
              1
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    textView14.check(matches(withText("Welcome stranger")))

    val textView15 = onView(
      allOf<View>(
        withText("No surveys available"),
        childAtPosition(
          allOf<View>(
            withId(R.id.noData),
            childAtPosition(
              withId(R.id.coordinatorLayout),
              1
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    textView15.check(matches(withText("No surveys available")))

    val textView16 = onView(
      allOf<View>(
        withText("Add a survey using\nthe big pink button"),
        childAtPosition(
          allOf<View>(
            withId(R.id.noData),
            childAtPosition(
              withId(R.id.coordinatorLayout),
              1
            )
          ),
          2
        ),
        isDisplayed()
      )
    )
    textView16.check(matches(withText("Add a survey using the big pink button")))

    val imageButton = onView(
      allOf<View>(
        withId(R.id.fabAddSurvey),
        childAtPosition(
          allOf<View>(
            withId(R.id.coordinatorLayout),
            childAtPosition(
              withId(R.id.surveyListRoot),
              1
            )
          ),
          2
        ),
        isDisplayed()
      )
    )
    imageButton.check(matches(isDisplayed()))

  }

  private fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
  ): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
      }

      public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return (parent is ViewGroup && parentMatcher.matches(parent)
          && view == parent.getChildAt(position))
      }
    }
  }*/
}
