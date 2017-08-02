package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.os.Bundle
import android.view.View
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.widget.MultiSpinner
import eu.mignot.pathogentracker.widget.multiSpinner
import kotlinx.android.synthetic.main.activity_add_human_survey.*
import kotlinx.android.synthetic.main.fragment_sample_info_1.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.contentView
import org.jetbrains.anko.info
import org.jetbrains.anko.yesButton

class AddHumanSurveyActivity: BaseSurveyActivity(), StepperLayout.StepperListener {

  private val vm by lazy {
    AddHumanViewModel()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_human_survey)
    setupToolbar(toolbarAH, getString(R.string.title_human))
    setupStepper()
  }

  override fun bind() {
    info("binding...")
  }

  override fun saveAndClose() {
    val sampleTypes = surveySampleTypes?.getAllValues().toString()
    alert("Sample types: $sampleTypes","Saving record...") {
      yesButton {}
    }.show()
  }

  private fun setupStepper() {
    stepperLayout.adapter = StepAdapter(supportFragmentManager, this, 3)
    stepperLayout.setListener(this)
  }

  override fun onStepSelected(newStepPosition: Int) {
    info("step selected: $newStepPosition")
  }

  override fun onError(verificationError: VerificationError?) {
    error("$verificationError")
  }

  override fun onReturn() {
    info("returning...")
  }

  override fun onCompleted(completeButton: View?) {
    saveAndClose()
  }

}
