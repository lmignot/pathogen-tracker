package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.util.AppMode
import eu.mignot.pathogentracker.data.models.User

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
  fun getUser(): User
  fun setUser(u: User)
  fun getDidCompleteOnBoarding(): Boolean
  fun setDidCompleteOnBoarding(b: Boolean)
  fun getPiNetworkId(): Int
  fun setPiNetworkId(i: Int)
}
