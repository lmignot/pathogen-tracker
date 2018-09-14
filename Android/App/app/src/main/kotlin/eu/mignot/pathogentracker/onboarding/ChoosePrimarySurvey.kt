package eu.mignot.pathogentracker.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.SurveyType
import kotlinx.android.synthetic.main.fragment_choose_primary_activity.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ChoosePrimarySurvey: Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_choose_primary_activity, container, false)
  }

  override fun onStart() {
    super.onStart()

    primaryActivityVector.onClick {
      (activity as OnBoarding).onChoosePrimaryActivity(SurveyType.VECTOR)
    }

    primaryActivityPatient.onClick {
      (activity as OnBoarding).onChoosePrimaryActivity(SurveyType.PATIENT)
    }
  }

  companion object {
      fun newInstance() = ChoosePrimarySurvey()
  }
}
