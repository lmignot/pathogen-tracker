package eu.mignot.pathogentracker.onboarding

import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.preferences.PreferencesProvider

class OnBoardingViewModel(private val prefs: PreferencesProvider) {

  private var chosenPrimaryActivity: SurveyType = SurveyType.NONE
  private var chosenSecondaryActivity: SurveyType = SurveyType.NONE

  /**
   * Resets preferences to default values
   */
  fun resetPreferences() {
    prefs.setPrimarySurveyActivity(SurveyType.NONE)
    prefs.setSecondarySurveyActivity(SurveyType.NONE)
    prefs.setDidCompleteOnBoarding(false)
  }

  private fun setPrimary(s: SurveyType) {
    chosenPrimaryActivity = s
    prefs.setPrimarySurveyActivity(s)
  }
  private fun setSecondary(s: SurveyType) {
    chosenSecondaryActivity = s
    prefs.setSecondarySurveyActivity(s)
  }

  /**
   * Set and get the primary survey type
   */
  var primaryActivity: SurveyType
    get() = chosenPrimaryActivity
    set(s) = setPrimary(s)

  /**
   * Set and get the secondary survey type
   */
  var secondaryActivity: SurveyType
    get() = chosenSecondaryActivity
    set(s) = setSecondary(s)

  /**
   * Set onBoarding to complete
   */
  fun setOnBoardingComplete(isComplete: Boolean) =
    prefs.setDidCompleteOnBoarding(isComplete)

}
