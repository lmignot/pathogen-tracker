package eu.mignot.pathogentracker.preferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import eu.mignot.pathogentracker.R

class AppPreferencesFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.preferences)
  }
}
