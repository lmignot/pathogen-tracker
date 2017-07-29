package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.util.AppMode
import eu.mignot.pathogentracker.surveys.data.models.User

/**
 * Provides preferences to the app
 */
interface PreferencesProvider {
  fun getAppMode(): AppMode
  fun setAppMode(m: AppMode): Unit
  fun getPrimarySurveyActivity(): SurveyType
  fun setPrimarySurveyActivity(s: SurveyType): Unit
  fun getSecondarySurveyActivity(): SurveyType
  fun setSecondarySurveyActivity(s: SurveyType): Unit
  fun getUseCellular(): Boolean
  fun setUseCellular(b: Boolean): Unit
  fun getOptimizeImageRes(): Boolean
  fun setOptimizeImageRes(b: Boolean): Unit
  fun getUser(): User
  fun setUser(u: User): Unit
  fun getDidCompleteOnboarding(): Boolean
  fun setDidCompleteOnboarding(b: Boolean): Unit
}
