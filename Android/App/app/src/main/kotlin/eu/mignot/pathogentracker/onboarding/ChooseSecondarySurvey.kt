package eu.mignot.pathogentracker.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.util.AppSettings
import kotlinx.android.synthetic.main.fragment_choose_secondary_activity.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ChooseSecondarySurvey : Fragment() {

  private fun getSecondaryActivityChoice(primary: SurveyType) = when (primary) {
    is SurveyType.PATIENT -> SurveyType.VECTOR
    is SurveyType.VECTOR -> SurveyType.PATIENT
    SurveyType.NONE -> SurveyType.NONE
  }

  /**
   * Get the users primary activity choice
   */
  private val primaryActivityChoice by lazy {
    SurveyType.get(arguments!!.getString(AppSettings.Constants.ACTIVITY_CHOICE))
  }

  /**
   * Determine the available secondary choice based
   * on the user's primary choice
   */
  private val secondaryActivityChoice by lazy {
    getSecondaryActivityChoice(primaryActivityChoice)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_choose_secondary_activity, container, false)
  }

  override fun onStart() {
    super.onStart()

    // set the question based on the available secondary choice
    questionTextSecondary.text = getString(R.string.what_is_secondary_activity, secondaryActivityChoice.toString())

    secondaryActivityTrue.onClick {
      (activity as OnBoarding).onChooseSecondaryActivity(secondaryActivityChoice)
    }

    secondaryActivityFalse.onClick {
      (activity as OnBoarding).onChooseSecondaryActivity(SurveyType.NONE)
    }
  }

  companion object {
      fun newInstance(s: SurveyType): ChooseSecondarySurvey {
        val f = ChooseSecondarySurvey()
        val args = Bundle()
        args.putString(AppSettings.Constants.ACTIVITY_CHOICE, s.toString())
        f.arguments = args
        return f
      }
  }
}
