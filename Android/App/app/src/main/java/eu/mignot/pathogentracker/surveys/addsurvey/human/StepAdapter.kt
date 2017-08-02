package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.content.Context
import android.support.v4.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import eu.mignot.pathogentracker.R

class StepAdapter(
  fm: FragmentManager,
  context: Context,
  val numSteps: Int
): AbstractFragmentStepAdapter(fm, context) {

  override fun createStep(position: Int): Step {
    when(position) {
      0 -> return StepFragment.newInstance(R.layout.fragment_personal_info)
      1 -> return StepFragment.newInstance(R.layout.fragment_sample_info_1)
      2 -> return StepFragment.newInstance(R.layout.fragment_sample_info_2)
      else -> throw IllegalArgumentException("incorrect step")
    }
  }

  override fun getCount(): Int = numSteps

}
