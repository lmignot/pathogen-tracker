package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import eu.mignot.pathogentracker.util.showShortMessage
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import kotlinx.android.synthetic.main.activity_add_human_survey.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

abstract class StepFragment : Fragment(), Step, AnkoLogger {

  /**
   * Resource ID of layout matching this step
   */
  abstract protected val layoutResourceId: Int

  /**
   * Add data to provided model and return it
   * @param model Model data will be added to
   * @return The modified model
   */
  abstract fun getModel(model: Human): Human

  val vm by lazy {
    (activity as AddHumanSurveyActivity).vm
  }

  /**
   * @see Fragment.onCreateView
   */
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(layoutResourceId, container, false)
  }

  /**
   * @see Step.onSelected
   */
  override fun onSelected() {
    info("on selected...")
  }

  /**
   * @see Step.verifyStep
   */
  override fun verifyStep(): VerificationError? {
    return null
  }

  /**
   * @see Step.onError
   */
  override fun onError(error: VerificationError) {
    showShortMessage(humanSurveyRoot, error.errorMessage)
  }

}
