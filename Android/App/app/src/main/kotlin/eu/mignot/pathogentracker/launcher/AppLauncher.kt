package eu.mignot.pathogentracker.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.auth.LoginProvider
import eu.mignot.pathogentracker.onboarding.OnBoarding
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings.RequestCodes.LOGIN_REQ_CODE
import org.jetbrains.anko.*

class AppLauncher : AppCompatActivity(), AnkoLogger {

  private val prefsProvider: PreferencesProvider by lazy {
    App.getPreferenceProvider()
  }

  private val loginProvider: LoginProvider by lazy {
    App.getLoginProvider()
  }

  private val loginUI: AuthUI by lazy {
    App.getLoginUI()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_launcher)

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
    if (requestCode == LOGIN_REQ_CODE) {
      val response = IdpResponse.fromResultIntent(data)
      if (resultCode == Activity.RESULT_OK) {
        if (prefsProvider.getDidCompleteOnBoarding()) {
          startActivity<SurveysActivity>()
        } else {
          startActivity<OnBoarding>()
        }
      } else {
        if (response == null) {
          alert(getString(R.string.login_error_sign_in)) {
            yesButton { doLogin() }
          }.show()
          return
        }
        if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
          alert(getString(R.string.error_login_no_network)) {
            yesButton {
              doLogin()
            }
          }.show()
          return
        }
        error { response.error.toString() }
      }
    }
  }

  private fun doLogin() {
    startActivityForResult(
      loginUI
        .createSignInIntentBuilder()
        .build(),
      LOGIN_REQ_CODE
    )
  }
}
