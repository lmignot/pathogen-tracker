package eu.mignot.pathogentracker.preferences

import eu.mignot.pathogentracker.surveys.data.SurveyType

/**
 * Provides preferences to the app
 */
interface PreferencesProvider {
  fun getPrimarySurveyActivity(): SurveyType
  fun setPrimarySurveyActivity(s: SurveyType)
  fun getSecondarySurveyActivity(): SurveyType
  fun setSecondarySurveyActivity(s: SurveyType)
  fun hasSecondarySurvey(): Boolean
  fun getUseCellular(): Boolean
  fun setUseCellular(b: Boolean)
  fun getOptimizeImageRes(): Boolean
  fun setOptimizeImageRes(b: Boolean)
  fun getDidCompleteOnBoarding(): Boolean
  fun setDidCompleteOnBoarding(b: Boolean)
  fun getPiNetworkId(): Int
  fun setPiNetworkId(i: Int)
}
