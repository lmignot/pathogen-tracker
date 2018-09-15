package eu.mignot.pathogentracker.preferences

import android.content.SharedPreferences
import android.preference.PreferenceManager
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.util.AppSettings

/**
 * Preference provider, Singleton utility class to handle
 * getting and setting user preferences
 *
 * @see PreferencesProvider
 */
object AppPreferencesProvider: PreferencesProvider {

  private val keys = AppSettings.PreferenceKeys

  private val sharedPreferences: SharedPreferences by lazy {
    PreferenceManager.getDefaultSharedPreferences(App.instance)
  }

  override fun getPrimarySurveyActivity(): SurveyType =
    SurveyType.get(sharedPreferences.getString(keys.PRIMARY_SURVEY_KEY, SurveyType.VECTOR.toString()))

  override fun setPrimarySurveyActivity(s: SurveyType) =
    sharedPreferences.edit().putString(keys.PRIMARY_SURVEY_KEY, s.toString()).apply()

  override fun getSecondarySurveyActivity(): SurveyType =
    SurveyType.get(sharedPreferences.getString(keys.SECONDARY_SURVEY_KEY, SurveyType.PATIENT.toString()))

  override fun setSecondarySurveyActivity(s: SurveyType) =
    sharedPreferences.edit().putString(keys.SECONDARY_SURVEY_KEY, s.toString()).apply()

  override fun hasSecondarySurvey(): Boolean =
    getSecondarySurveyActivity() != SurveyType.NONE

  override fun getUseCellular(): Boolean =
    sharedPreferences.getBoolean(keys.USE_CELLULAR_KEY, false)

  override fun setUseCellular(b: Boolean) =
    sharedPreferences.edit().putBoolean(keys.USE_CELLULAR_KEY, b).apply()

  override fun getOptimizeImageRes(): Boolean =
    sharedPreferences.getBoolean(keys.IMAGE_RESOLUTION_KEY, false)

  override fun setOptimizeImageRes(b: Boolean) =
    sharedPreferences.edit().putBoolean(keys.IMAGE_RESOLUTION_KEY, b).apply()

  override fun getDidCompleteOnBoarding(): Boolean =
    sharedPreferences.getBoolean(keys.ON_BOARDING_COMPLETE_KEY, false)

  override fun setDidCompleteOnBoarding(b: Boolean) =
    sharedPreferences.edit().putBoolean(keys.ON_BOARDING_COMPLETE_KEY, b).apply()

  override fun getPiNetworkId(): Int =
    sharedPreferences.getInt(keys.PI_NETWORK_ID_KEY, -1)

  override fun setPiNetworkId(i: Int) =
    sharedPreferences.edit().putInt(keys.PI_NETWORK_ID_KEY, i).apply()
}

