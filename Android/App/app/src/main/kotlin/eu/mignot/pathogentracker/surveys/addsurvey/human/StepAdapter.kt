package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.content.Context
import android.support.v4.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import org.jetbrains.anko.AnkoLogger

/**
 * @see AbstractFragmentStepAdapter
 */
class StepAdapter(
  fm: FragmentManager,
  context: Context,
  private val numSteps: Int
): AbstractFragmentStepAdapter(fm, context), AnkoLogger {

  /**
   * @see AbstractFragmentStepAdapter.createStep
   */
  override fun createStep(position: Int): Step {
    return when(position) {
      0 -> HumanPersonalInfo()
      1 -> HumanSampleInfoA()
      2 -> HumanSampleInfoB()
      else -> throw IllegalArgumentException("incorrect step")
    }
  }

  /**
   * @see AbstractFragmentStepAdapter.getCount
   */
  override fun getCount(): Int = numSteps

  /**
   * Returns a list of all [StepFragment] steps that have been initialised
   * @return List of initialised steps as [StepFragment]
   */
  fun getAllSteps(): List<StepFragment> =
    (0 until count).fold(listOf()) { ls: List<StepFragment>, i: Int ->
      val step = findStep(i)?.let { it as StepFragment }
      if (step != null) ls.plus(step) else ls
    }

}
