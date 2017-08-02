package eu.mignot.pathogentracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import eu.mignot.pathogentracker.extensions.showShortMessage
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchSurveyActivity
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorSurveyActivity
import eu.mignot.pathogentracker.util.AppSettings
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity: AppCompatActivity(), AnkoLogger {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupButtons()
    intent.getStringExtra(AppSettings.Constants.MESSAGE_KEY)?.let {
      showShortMessage(mainView, it)
    }
  }

  private fun setupButtons() {
    humanSurveyButton.setOnClickListener {
      info("Launching activity: HumanSurveyActivity")
      startActivity<AddHumanSurveyActivity>()
    }
    vectorBatchSurveyButton.setOnClickListener {
      info("Launching activity: AddVectorBatchSurveyActivity")
      startActivity<AddVectorBatchSurveyActivity>()
    }
    vectorSurveyButton.setOnClickListener {
      info("Launching activity: AddVectorSurveyActivity")
      startActivity<AddVectorSurveyActivity>(
        AppSettings.Constants.BATCH_ID_KEY to "VB-${UUID.randomUUID()}",
        AppSettings.Constants.VECTOR_ID_KEY to "V-${UUID.randomUUID()}-001"
      )
    }
  }
}
