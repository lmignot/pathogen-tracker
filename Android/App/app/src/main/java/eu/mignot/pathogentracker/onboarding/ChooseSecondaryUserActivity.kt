package eu.mignot.pathogentracker.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.util.AppSettings
import kotlinx.android.synthetic.main.fragment_choose_secondary_activity.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ChooseSecondaryUserActivity : Fragment() {

  private fun getSecondaryActivityChoice(primary: SurveyType) = when (primary) {
    is SurveyType.PATIENT -> SurveyType.VECTOR()
    is SurveyType.VECTOR -> SurveyType.PATIENT()
    SurveyType.NONE -> SurveyType.NONE
  }

  private val primaryActivityChoice by lazy {
    SurveyType.get(arguments.getString(AppSettings.Constants.ACTIVITY_CHOICE))
  }

  private val secondaryActivityChoice by lazy {
    getSecondaryActivityChoice(primaryActivityChoice)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_choose_secondary_activity, container, false)
  }

  override fun onStart() {
    super.onStart()

    questionTextSecondary.text = getString(R.string.what_is_secondary_activity, secondaryActivityChoice.toString())

    secondaryActivityTrue.onClick {
      (activity as OnBoarding).onChooseSecondaryActivity(secondaryActivityChoice)
    }

    secondaryActivityFalse.onClick {
      (activity as OnBoarding).onChooseSecondaryActivity(SurveyType.NONE)
    }
  }

  companion object {
      fun newInstance(s: SurveyType): ChooseSecondaryUserActivity {
        val f = ChooseSecondaryUserActivity()
        val args = Bundle()
        args.putString(AppSettings.Constants.ACTIVITY_CHOICE, s.toString())
        f.arguments = args
        return f
      }
  }
}
