package eu.mignot.pathogentracker.preferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.util.AppSettings.PreferenceKeys.PRIMARY_SURVEY_KEY
import eu.mignot.pathogentracker.util.AppSettings.PreferenceKeys.SECONDARY_SURVEY_KEY
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class AppPreferencesFragment: PreferenceFragmentCompat() {

  private val prefsProvider by lazy {
    App.getPreferenceProvider()
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val primarySurvey = findPreference(PRIMARY_SURVEY_KEY)
    val secondarySurvey = findPreference(SECONDARY_SURVEY_KEY)

    primarySurvey.setOnPreferenceChangeListener { _, newValue: Any ->
      val secondaryPref: String = prefsProvider.getSecondarySurveyActivity().toString()
      val primaryPref: String = newValue as String
      if (secondaryPref == primaryPref) {
        alert("Secondary survey already set to $secondaryPref") {
          yesButton {}
        }.show()
      }
      secondaryPref != primaryPref
    }

    // prevent the user from setting secondary and primary surveys to the same type
    secondarySurvey.setOnPreferenceChangeListener { _, newValue: Any ->
      val primaryPref: String = prefsProvider.getPrimarySurveyActivity().toString()
      val secondaryPref: String = newValue as String
      if (secondaryPref == "None") {
        true
      } else {
        if (secondaryPref == primaryPref) {
          alert("Primary survey already set to $primaryPref") {
            yesButton {}
          }.show()
        }
        secondaryPref != primaryPref
      }
    }

  }

}
