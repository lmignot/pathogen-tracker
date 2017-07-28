package eu.mignot.pathogentracker

import android.app.Application
import com.patloew.rxlocation.RxLocation
import eu.mignot.pathogentracker.data.AppLocationProvider

class App : Application() {

  val locationProvider by lazy {
    AppLocationProvider(RxLocation(this))
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    lateinit var instance: App private set
    fun getLocationProvider() = instance.locationProvider
  }

}
