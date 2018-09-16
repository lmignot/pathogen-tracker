package eu.mignot.pathogentracker

import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object TestCommon {

  private const val TEST_USER_EMAIl = "testuser@pathogentrackerapp.gmail.com"
  private const val TEST_USER_PASSWORD = "as\$h26mPO2zF"

  private val loginProvider = App.getLoginProvider()

  fun signOut() {
    loginProvider.signOut()
  }

  fun signIn() {
    FirebaseAuth
      .getInstance()
      .signInWithEmailAndPassword(TEST_USER_EMAIl, TEST_USER_PASSWORD)
  }

  fun childAtPosition(
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
  }
}
