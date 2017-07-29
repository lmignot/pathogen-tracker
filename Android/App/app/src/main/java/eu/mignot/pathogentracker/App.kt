package eu.mignot.pathogentracker

import android.app.Application
import com.patloew.rxlocation.RxLocation
import eu.mignot.pathogentracker.data.AppLocationProvider
import eu.mignot.pathogentracker.data.AppPreferencesProvider
import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.data.PreferencesProvider

class App : Application() {

  val locationProvider by lazy {
    AppLocationProvider(RxLocation(this))
  }

  val prefsProvider by lazy { AppPreferencesProvider }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    lateinit var instance: App private set
    fun getLocationProvider(): LocationProvider = instance.locationProvider
    fun getPreferenceProvider(): PreferencesProvider = instance.prefsProvider
  }

}
