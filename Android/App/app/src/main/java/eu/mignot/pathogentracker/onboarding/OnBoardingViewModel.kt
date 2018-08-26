package eu.mignot.pathogentracker.onboarding

import eu.mignot.pathogentracker.data.PreferencesProvider
import eu.mignot.pathogentracker.surveys.data.SurveyType

class OnBoardingViewModel(private val prefs: PreferencesProvider) {

  private var chosenPrimaryActivity: SurveyType = SurveyType.NONE
  private var chosenSecondaryActivity: SurveyType = SurveyType.NONE

  private fun setPrimary(s: SurveyType) {
    chosenPrimaryActivity = s
    prefs.setPrimarySurveyActivity(s)
  }
  private fun setSecondary(s: SurveyType) {
    chosenSecondaryActivity = s
    prefs.setSecondarySurveyActivity(s)
  }

  var primaryActivity: SurveyType
    get() = chosenPrimaryActivity
    set(s) = setPrimary(s)

  var secondaryActivity: SurveyType
    get() = chosenSecondaryActivity
    set(s) = setSecondary(s)

  fun setOnBoardingComplete(isComplete: Boolean) =
    prefs.setDidCompleteOnBoarding(isComplete)

}
