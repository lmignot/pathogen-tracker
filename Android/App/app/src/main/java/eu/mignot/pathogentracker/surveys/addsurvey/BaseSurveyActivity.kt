package eu.mignot.pathogentracker.surveys.addsurvey

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import io.reactivex.disposables.CompositeDisposable
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.*

abstract class BaseSurveyActivity: AppCompatActivity(), AddSurvey, AnkoLogger {

  val disposables by lazy {
    CompositeDisposable()
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

  override fun setupToolbar(toolbar: Toolbar, title: String) {
    setSupportActionBar(toolbar)
    supportActionBar?.title = title
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.close_white)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.actionSave -> {
        saveAndClose()
        return true
      }
      android.R.id.home -> {
        cancelAndClose()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
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
    alert(getString(R.string.cancel_survey_title),getString(R.string.cancel_survey_rationale)){
      positiveButton(getString(R.string.leave)) {
        startActivity<MainActivity>()
      }
      negativeButton(R.string.stay) {}
    }.show()
  }
}
