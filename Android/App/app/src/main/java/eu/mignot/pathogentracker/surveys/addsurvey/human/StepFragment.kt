package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

class StepFragment : Fragment(), Step, AnkoLogger {

  companion object {
    const val LAYOUT_RESOURCE_KEY = "resourceId"

    fun newInstance(layoutResourceId: Int): StepFragment {
      val args = Bundle()
      args.putInt(LAYOUT_RESOURCE_KEY, layoutResourceId)
      val fragment = StepFragment()
      fragment.arguments = args
      return fragment
    }
  }

  val layoutResourceId: Int by lazy {
    arguments.getInt(LAYOUT_RESOURCE_KEY)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(layoutResourceId, container, false)
  }

  override fun onSelected() {
    info("on selected...")
  }

  override fun verifyStep(): VerificationError? {
    return null
  }

  override fun onError(error: VerificationError) {
    debug(error)
  }

}
