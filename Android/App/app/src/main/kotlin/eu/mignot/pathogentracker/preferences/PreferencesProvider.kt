package eu.mignot.pathogentracker.preferences

import eu.mignot.pathogentracker.data.SurveyType

/**
 * Provides preferences to the app
 *
 * Methods are all getters/setters and self-explanatory
 */
interface PreferencesProvider {
  fun getPrimarySurveyActivity(): SurveyType
  fun setPrimarySurveyActivity(s: SurveyType): Boolean
  fun getSecondarySurveyActivity(): SurveyType
  fun setSecondarySurveyActivity(s: SurveyType): Boolean
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
