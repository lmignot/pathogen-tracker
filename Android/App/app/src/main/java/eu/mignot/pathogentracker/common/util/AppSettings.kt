package eu.mignot.pathogentracker.common.util

object PreferenceKeys {
  const val PREFERENCES_FILE_NAME = "pathogen_tracker_app_preferences"
  const val APP_MODE_KEY = "APP_MODE_KEY"
  const val PRIMARY_SURVEY_KEY = "PRIMARY_SURVEY_KEY"
  const val SECONDARY_SURVEY_KEY = "SECONDARY_SURVEY_KEY"
  const val HAS_SECONDARY_SURVEY_KEY = "HAS_SECONDARY_SURVEY_KEY"
  const val USE_CELLULAR_KEY = "USE_CELLULAR_KEY"
  const val OPTIMIZE_IMAGE_RESOLUTION_KEY = "OPTIMIZE_IMAGE_RESOLUTION_KEY"
  const val AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
  const val USER_NAME_KEY = "USER_NAME_KEY"
  const val USER_EMAIL_KEY = "USER_EMAIL_KEY"
  const val USER_ID_KEY = "USER_ID_KEY"
}

enum class SurveyType {
  VECTOR, HUMAN, NA
}

enum class AppMode {
  DEMO, LIVE
}
