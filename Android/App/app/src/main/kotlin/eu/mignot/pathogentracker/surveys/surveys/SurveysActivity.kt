package eu.mignot.pathogentracker.surveys.surveys

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.launcher.AppLauncher
import eu.mignot.pathogentracker.preferences.AppPreferencesActivity
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchSurveyActivity
import eu.mignot.pathogentracker.syncservice.FirebaseHumanSyncService
import eu.mignot.pathogentracker.syncservice.FirebasePhotoSyncService
import eu.mignot.pathogentracker.syncservice.FirebaseVectorBatchSyncService
import eu.mignot.pathogentracker.syncservice.FirebaseVectorSyncService
import eu.mignot.pathogentracker.util.AppSettings.Constants
import eu.mignot.pathogentracker.util.setupToolbar
import eu.mignot.pathogentracker.util.showShortMessage
import kotlinx.android.synthetic.main.activity_surveys.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SurveysActivity: AppCompatActivity(), AnkoLogger {

  private val jobScheduler: JobScheduler by lazy {
    (applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)) as JobScheduler
  }

  private val prefsProvider by lazy {
    App.getPreferenceProvider()
  }

  private val loginProvider by lazy {
    App.getLoginProvider()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!loginProvider.hasUser()) {
      startActivity<AppLauncher>()
    }

    setContentView(R.layout.activity_surveys)

    intent.getStringExtra(Constants.MESSAGE_KEY)?.let {
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

  override fun onStart() {
    super.onStart()
    info { "TRYING TO START THE JOBS"}
    if (loginProvider.hasUser()) {
      startJobs()
    }
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(navView)) {
      drawerLayout.closeDrawer(navView)
    }
  }

  private fun startJobs() {
    startJob(FirebaseHumanSyncService::class.java, Constants.JOB_ID_HUMANS)
    startJob(FirebaseVectorBatchSyncService::class.java, Constants.JOB_ID_VECTOR_BATCHES)
    startJob(FirebaseVectorSyncService::class.java, Constants.JOB_ID_VECTORS)
    startJob(FirebasePhotoSyncService::class.java, Constants.JOB_ID_PHOTOS)
  }

  private fun startJob(cls: Class<*>, id: Int) {
    info { "START JOB CALLED"}
    val componentName = getComponentName(cls)
    val job: JobInfo = getJobInfo(id, prefsProvider.getUseCellular(), componentName)
    jobScheduler.schedule(job)
    info { "JOB SCHEDULED"}
  }

  private fun getComponentName(cls: Class<*>): ComponentName =
    ComponentName(this, cls)

  /**
   * Configure a background upload job
   *
   */
  private fun getJobInfo(id: Int, useCellular: Boolean, componentName: ComponentName): JobInfo {
    return when (useCellular) {
      true -> JobInfo.Builder(id, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPersisted(true)
        .build()
      false -> JobInfo.Builder(id, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        .setPersisted(true)
        .build()
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
          prefsProvider.setDidCompleteOnBoarding(false)
          doLogout()
          drawerLayout.closeDrawer(navView)
          true
        }
        R.id.menuItemSettings -> {
          showSettings()
          drawerLayout.closeDrawer(navView)
          true
        }
        else -> true
      }
    }
  }

  private fun doLogout() {
    loginProvider.signOut()
    startActivity<AppLauncher>()
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
