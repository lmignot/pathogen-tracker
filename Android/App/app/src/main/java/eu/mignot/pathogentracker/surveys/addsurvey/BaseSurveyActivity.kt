package eu.mignot.pathogentracker.surveys.addsurvey

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.*

abstract class BaseSurveyActivity: AppCompatActivity(), AddSurvey {

  override fun onResume() {
    super.onResume()
    bind()
  }

  override fun onPause() {
    unbind()
    super.onPause()
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
      yesButton {
        startActivity<MainActivity>()
      }
      noButton {}
    }.show()
  }
}
