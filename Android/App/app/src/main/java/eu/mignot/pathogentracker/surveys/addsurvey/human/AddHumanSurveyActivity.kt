package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.os.Bundle
import android.view.View
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.setupToolbar
import kotlinx.android.synthetic.main.activity_add_human_survey.*
import org.jetbrains.anko.*

class AddHumanSurveyActivity: BaseSurveyActivity<Human>(), StepperLayout.StepperListener {

  override val vm by lazy {
    AddHumanViewModel(
      App.getFormDataProvider(),
      App.getSurveysRepository()
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_human_survey)
    setupToolbar(toolbarAH, getString(R.string.title_human), R.drawable.close_white)
    setupStepper()
  }

  override fun unbind() {
    info("Unbinding...")
  }

  override fun saveAndClose() {
    val model = getModel()
    info("Saving.... $model")
    doAsync {
      vm.save(model)
      uiThread {
        startActivity<SurveysActivity>(
          AppSettings.Constants.MESSAGE_KEY to "Survey ${model.id} added to database"
        )
        finish()
      }
    }
  }

  override fun getModel(): Human =
    (stepperLayout.adapter as StepAdapter)
      .getAllSteps().fold( Human(), { model, step ->
        step.getModel(model)
      })

  private fun setupStepper() {
    stepperLayout.adapter = StepAdapter(supportFragmentManager, this, 3)
    stepperLayout.setListener(this)
  }

  override fun onStepSelected(newStepPosition: Int) {
    info("step selected: $newStepPosition")
    stepperLayout.adapter.findStep(newStepPosition)?.verifyStep()
  }

  override fun onError(verificationError: VerificationError?) {
    error("$verificationError")
  }

  override fun onReturn() {
    info("returning...")
  }

  override fun onCompleted(completeButton: View?) = saveAndClose()

}
