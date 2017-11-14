package eu.mignot.pathogentracker.data

import android.content.SharedPreferences
import android.preference.PreferenceManager
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.data.models.User
import eu.mignot.pathogentracker.util.AppSettings

/**
 * Preference provider, Singleton utility class to handle
 * getting and setting user preferences
 */
object AppPreferencesProvider: PreferencesProvider {

  private val keys = AppSettings.PreferenceKeys

  private val sharedPreferences: SharedPreferences by lazy {
    PreferenceManager.getDefaultSharedPreferences(App.instance)
  }

  override fun getPrimarySurveyActivity(): SurveyType =
    SurveyType.get(sharedPreferences.getString(keys.PRIMARY_SURVEY_KEY, SurveyType.VECTOR().toString()))

  override fun setPrimarySurveyActivity(s: SurveyType) =
    sharedPreferences.edit().putString(keys.PRIMARY_SURVEY_KEY, s.toString()).apply()

  override fun getSecondarySurveyActivity(): SurveyType =
    SurveyType.get(sharedPreferences.getString(keys.SECONDARY_SURVEY_KEY, SurveyType.PATIENT().toString()))

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

  override fun getUser(): User =
    User(
      sharedPreferences.getLong(keys.USER_ID_KEY, -1L),
      sharedPreferences.getString(keys.USER_NAME_KEY, ""),
      sharedPreferences.getString(keys.USER_EMAIL_KEY, ""),
      sharedPreferences.getString(keys.AUTH_TOKEN_KEY, "")
    )

  override fun setUser(u: User) =
    sharedPreferences
      .edit()
      .putLong(keys.USER_ID_KEY, u.userId)
      .putString(keys.USER_NAME_KEY, u.name)
      .putString(keys.USER_EMAIL_KEY, u.email)
      .putString(keys.AUTH_TOKEN_KEY, u.token)
      .apply()

  override fun getDidCompleteOnBoarding(): Boolean =
    sharedPreferences.getBoolean(keys.ON_BOARDING_COMPLETE_KEY, false)

  override fun setDidCompleteOnBoarding(b: Boolean) =
    sharedPreferences.edit().putBoolean(keys.ON_BOARDING_COMPLETE_KEY, b).apply()

  override fun getPiNetworkId(): Int =
    sharedPreferences.getInt(keys.PI_NETWORK_ID_KEY, -1)

  override fun setPiNetworkId(i: Int) =
    sharedPreferences.edit().putInt(keys.PI_NETWORK_ID_KEY, i).apply()

  override fun getImageQuality(): Int =
    sharedPreferences.getInt(keys.IMAGE_QUALITY_KEY, AppSettings.Constants.DEFAULT_IMAGE_QUALITY)

  override fun setImageQuality(i: Int) =
    sharedPreferences.edit().putInt(keys.IMAGE_QUALITY_KEY, i).apply()
}

