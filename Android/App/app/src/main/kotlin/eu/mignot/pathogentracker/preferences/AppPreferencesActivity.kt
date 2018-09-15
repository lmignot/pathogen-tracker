package eu.mignot.pathogentracker.preferences

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.util.setupToolbar
import kotlinx.android.synthetic.main.activity_preferences.*

/**
 * Displays the preferences fragment
 */
class AppPreferencesActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_preferences)
    setupToolbar(toolbarP, "Preferences", R.drawable.arrow_back_white)
    supportFragmentManager.beginTransaction()
      .add(R.id.prefContainer, AppPreferencesFragment())
      .commit()
  }

}
