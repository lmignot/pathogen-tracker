package eu.mignot.pathogentracker.data

import android.content.Context
import android.content.SharedPreferences
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.SurveyType
import eu.mignot.pathogentracker.surveys.data.models.User
import eu.mignot.pathogentracker.util.AppMode
import eu.mignot.pathogentracker.util.AppSettings

/**
 * Preference provider, Singleton utility class to handle
 * getting and setting user preferences
 */
object AppPreferencesProvider: PreferencesProvider {

  private val keys = AppSettings.PreferenceKeys

  val sharedPreferences: SharedPreferences by lazy {
    context.getSharedPreferences(keys.PREFS_FILE_NAME, Context.MODE_PRIVATE)
  }

  val context: Context by lazy { App.instance }

  override fun getAppMode(): AppMode =
    AppMode.valueOf(sharedPreferences.getString(keys.APP_MODE_KEY, AppMode.LIVE.toString()))

  override fun setAppMode(m: AppMode) =
    sharedPreferences.edit().putString(keys.APP_MODE_KEY, m.toString()).apply()

  override fun getPrimarySurveyActivity(): SurveyType =
    SurveyType.valueOf(sharedPreferences.getString(keys.PRIMARY_SURVEY_KEY, SurveyType.HUMAN.toString()))

  override fun setPrimarySurveyActivity(s: SurveyType) =
    sharedPreferences.edit().putString(keys.PRIMARY_SURVEY_KEY, s.toString()).apply()

  override fun getSecondarySurveyActivity(): SurveyType =
    SurveyType.valueOf(sharedPreferences.getString(keys.SECONDARY_SURVEY_KEY, SurveyType.NA.toString()))

  override fun setSecondarySurveyActivity(s: SurveyType) =
    sharedPreferences.edit().putString(keys.PRIMARY_SURVEY_KEY, s.toString()).apply()

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

  override fun getDidCompleteOnboarding(): Boolean =
    sharedPreferences.getBoolean(keys.ONBOARDING_COMPLETE_KEY, false)

  override fun setDidCompleteOnboarding(b: Boolean) =
    sharedPreferences.edit().putBoolean(keys.ONBOARDING_COMPLETE_KEY, b).apply()
}

