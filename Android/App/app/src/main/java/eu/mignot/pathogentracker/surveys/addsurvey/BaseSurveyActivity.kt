package eu.mignot.pathogentracker.surveys.addsurvey

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import org.jetbrains.anko.*

abstract class BaseSurveyActivity: AppCompatActivity(), AddSurvey, AnkoLogger {

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

  override fun onResume() {
    super.onResume()
    bind()
  }

  override fun onPause() {
    unbind()
    super.onPause()
  }

  override fun cancelAndClose() {
    alert("Are you sure?","This record has not been saved..."){
      yesButton {
        startActivity<MainActivity>()
      }
      noButton {}
    }.show()
  }
}
