package eu.mignot.pathogentracker.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.onboarding.OnBoarding
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.DoesLogin
import eu.mignot.pathogentracker.util.showShortMessage
import kotlinx.android.synthetic.main.content_launcher.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

class AppLauncher : AppCompatActivity(), AnkoLogger {

  private val prefsProvider by lazy {
    App.getPreferenceProvider()
  }

  private val loginProvider by lazy {
    App.getLoginProvider()
  }

  private val loginUI by lazy {
    App.getLoginUI()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!loginProvider.hasUser()) {
      doLogin()
    } else if (!prefsProvider.getDidCompleteOnBoarding()) {
      startActivity<OnBoarding>()
    } else {
      startActivity<SurveysActivity>()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == DoesLogin.REQUEST_CODE) {
      val response = IdpResponse.fromResultIntent(data)
      if (resultCode == Activity.RESULT_OK) {
        if (prefsProvider.getDidCompleteOnBoarding()) {
          startActivity<SurveysActivity>()
        } else {
          startActivity<OnBoarding>()
        }
      } else {
        if (response == null) {
          showShortMessage(launcherRoot, "This app requires a signed-in user, please sign in.")
          doLogin()
          return
        }
        if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
          showShortMessage(launcherRoot, "Please enable a network connection and try signing in again.")
          doLogin()
          return
        }
        showShortMessage(launcherRoot, "We encountered an unknown error when signing in sorry :(")
        error { response.error.toString() }
      }
    }
  }

  private fun doLogin() {
    startActivityForResult(
      loginUI
        .createSignInIntentBuilder()
        .build(),
      DoesLogin.REQUEST_CODE
    )
  }
}
