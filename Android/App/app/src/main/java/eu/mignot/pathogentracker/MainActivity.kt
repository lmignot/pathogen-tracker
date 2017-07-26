package eu.mignot.pathogentracker

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchSurveyActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity: AppCompatActivity() {

  companion object {
      const val TAG = "MAIN_ACTIVITY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupButtons()
  }

  private fun setupButtons() {
    humanSurveyButton.setOnClickListener {
      Snackbar
        .make(window.decorView.rootView, "Human Survey launching...", Snackbar.LENGTH_SHORT)
        .show()
    }
    vectorBatchSurveyButton.setOnClickListener {
      Log.d(TAG, "Launching activity: AddVectorBatchSurveyActivity")
      startActivity<AddVectorBatchSurveyActivity>()
    }
  }
}
