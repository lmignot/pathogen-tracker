package eu.mignot.pathogentracker.surveys.surveys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.onboarding.OnBoarding
import eu.mignot.pathogentracker.preferences.AppPreferencesActivity
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchSurveyActivity
import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.DoesLogin
import eu.mignot.pathogentracker.util.setupToolbar
import eu.mignot.pathogentracker.util.showShortMessage
import kotlinx.android.synthetic.main.activity_surveys.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SurveysActivity: AppCompatActivity(), AnkoLogger {

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

    doLogin()

    if (!prefsProvider.getDidCompleteOnBoarding()) {
      startActivity<OnBoarding>()
    }

    setContentView(R.layout.activity_surveys)

    intent.getStringExtra(AppSettings.Constants.MESSAGE_KEY)?.let {
      showShortMessage(surveyListRoot, it)
    }

    // setup the toolbar
    setupToolbar(toolbar, getString(R.string.title_activity_surveys), R.drawable.menu)

    // setup the navigation drawer
    setupDrawer()

    // setup the surveys fragment
    setupSurveysFragment(savedInstanceState)

    fabAddSurvey.onClick {
      // if there is no secondary survey, pick the appropriate option
      if (!prefsProvider.hasSecondarySurvey()) {
        when(prefsProvider.getPrimarySurveyActivity()) {
          is SurveyType.PATIENT -> startActivity<AddHumanSurveyActivity>()
          is SurveyType.VECTOR -> startActivity<AddVectorBatchSurveyActivity>()
          is SurveyType.NONE -> showShortMessage(surveyListRoot, getString(R.string.error_no_primary_activity))
        }
      } else {
        showSurveySelection()
      }
    }
  }

  /**
   * @see [AppCompatActivity.onOptionsItemSelected]
   */
  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        drawerLayout.openDrawer(GravityCompat.START)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == DoesLogin.REQUEST_CODE) {
      val response = IdpResponse.fromResultIntent(data)
      if (resultCode != Activity.RESULT_OK) {
        if (response == null) {
          showShortMessage(surveyListRoot, "This app requires a signed-in user, please sign in.")
          doLogin()
          return
        }
        if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
          showShortMessage(surveyListRoot, "Please enable a network connection and try signing in again.")
          doLogin()
          return
        }
        showShortMessage(surveyListRoot, "We encountered an unknown error when signing in sorry :(")
        error { response.error.toString() }
      }
    }
  }

  private fun setupSurveysFragment(savedInstanceState: Bundle?) {
    if (savedInstanceState == null) {
      supportFragmentManager
        .beginTransaction()
        .add(R.id.contentFrame, SurveysFragment.newInstance())
        .commit()
    }
  }

  private fun setupDrawer() {
    drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
    setupDrawerNavigation(navView)
  }

  private fun setupDrawerNavigation(nView: NavigationView) {
    nView.setNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.menuItemLogout -> {
          doLogout()
          drawerLayout.closeDrawers()
          true
        }
        R.id.menuItemSettings -> {
          showSettings()
          drawerLayout.closeDrawers()
          true
        }
        else -> true
      }
    }
  }

  private fun doLogin() {
    if (!loginProvider.hasUser()) {
      startActivityForResult(
        loginUI
          .createSignInIntentBuilder()
          .build(),
        DoesLogin.REQUEST_CODE
      )
    }
  }

  private fun doLogout() {
    showShortMessage(surveyListRoot, "Logout menu button clicked")
  }

  private fun showSettings() {
    startActivity<AppPreferencesActivity>()
  }

  /**
   * Creates and shows a dialog giving the user
   * a choice of which survey they'd like to take
   */
  private fun showSurveySelection() {
    alert {
      customView {
        linearLayout {
          orientation = LinearLayout.VERTICAL
          padding = dip(24)
          lparams(matchParent, wrapContent)
          textView(context.getString(R.string.survey_type_select)) {
            textSize = 22f
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
          }.lparams(matchParent, wrapContent)
          button(getString(R.string.survey_type_human)) {
            backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            textColor = ContextCompat.getColor(context, R.color.colorIcons)
            gravity = Gravity.CENTER_HORIZONTAL
            onClick {
              startActivity<AddHumanSurveyActivity>()
            }
          }.lparams(matchParent, wrapContent) {
            topMargin = dip(32)
          }
          button(getString(R.string.survey_type_vector)) {
            backgroundColor = ContextCompat.getColor(context, R.color.colorVectorPrimaryDark)
            textColor = ContextCompat.getColor(context, R.color.colorIcons)
            gravity = Gravity.CENTER_HORIZONTAL
            onClick {
              startActivity<AddVectorBatchSurveyActivity>()
            }
          }.lparams(matchParent, wrapContent) {
            topMargin = dip(24)
          }
        }
      }
      negativeButton("Cancel") {}
    }.show()
  }

}
