package eu.mignot.pathogentracker.surveys.addsurvey

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmObject
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

abstract class BaseSurveyActivity<T: RealmObject>: AppCompatActivity(), AddSurvey<T>, AnkoLogger {

  abstract val vm: BaseViewModel<T>

  val disposables by lazy {
    CompositeDisposable()
  }

  val preferences by lazy {
    App.getPreferenceProvider()
  }

  override fun onResume() {
    super.onResume()
    bind()
  }

  override fun onPause() {
    unbind()
    super.onPause()
  }

  override fun unbind() {
    disposables.dispose()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.actionSave -> {
        saveAndClose()
        true
      }
      android.R.id.home -> {
        cancelAndClose()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.add_survey_menu_white, menu)
    return true
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    EffortlessPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
  }

  override fun cancelAndClose() {
    alert(getString(R.string.cancel_survey_title), getString(R.string.cancel_survey_rationale)){
      positiveButton(getString(R.string.action_leave)) {
        startActivity<SurveysActivity>()
      }
      negativeButton(R.string.action_stay) {}
    }.show()
  }
}
