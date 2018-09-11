package eu.mignot.pathogentracker.data

import android.os.Bundle
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.listener.LocationListener

/**
 * LocationView
 *
 * Describes methods used by an activity that require fetching
 * a device's current location.
 */
interface LocationView: LocationListener {

  fun getLocationManager(): LocationManager

  fun getLocation() = getLocationManager().get()

  fun processLocation(location: android.location.Location?)

  fun processLocationError(type: Int)

  fun dismissLocationProgress()

  fun showLocationProgress()

  fun gpsPermissionGranted(alreadyHadPermission: Boolean)

  override fun onLocationChanged(location: android.location.Location?) {
    dismissLocationProgress()
    processLocation(location)
  }

  override fun onLocationFailed(type: Int) {
    dismissLocationProgress()
    processLocationError(type)
  }

  override fun onProviderEnabled(provider: String?) {}

  override fun onProviderDisabled(provider: String?) {}

  override fun onProcessTypeChanged(processType: Int) {}

  override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

  override fun onPermissionGranted(alreadyHadPermission: Boolean) = gpsPermissionGranted(alreadyHadPermission)

}
