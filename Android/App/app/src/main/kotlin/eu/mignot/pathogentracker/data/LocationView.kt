package eu.mignot.pathogentracker.data

import android.os.Bundle
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.listener.LocationListener

/**
 * LocationView
 *
 * Describes methods used by an activity that require fetching
 * a device's current location.
 *
 * Uses [com.yayandroid.locationmanager.LocationManager] to manage
 * interacting with FusedLocationProvider
 */
interface LocationView: LocationListener {

  /**
   * Build the LocationManager instance
   */
  fun getLocationManager(): LocationManager

  /**
   * @see LocationManager.get
   */
  fun getLocation() = getLocationManager().get()

  /**
   * Handle a returned location value
   * @param location, the possibly null Android Location object
   */
  fun processLocation(location: android.location.Location?)

  /**
   * Handle a location request error
   * @param type the type of error returned
   */
  fun processLocationError(type: Int)

  /**
   * Dismiss the location progress indicator
   */
  fun dismissLocationProgress()

  /**
   * Display the location progress indicator
   */
  fun showLocationProgress()

  /**
   * Callback for when a user has granted permission
   *
   * @param alreadyHadPermission whether the user has previously granted permission or not
   */
  fun gpsPermissionGranted(alreadyHadPermission: Boolean)

  /**
   * @see LocationListener.onLocationChanged
   */
  override fun onLocationChanged(location: android.location.Location?) {
    dismissLocationProgress()
    processLocation(location)
  }

  /**
   * @see LocationListener.onLocationFailed
   */
  override fun onLocationFailed(type: Int) {
    dismissLocationProgress()
    processLocationError(type)
  }

  /**
   * @see LocationListener.onProviderEnabled
   */
  override fun onProviderEnabled(provider: String?) {}

  /**
   * @see LocationListener.onProviderDisabled
   */
  override fun onProviderDisabled(provider: String?) {}

  /**
   * @see LocationListener.onProcessTypeChanged
   */
  override fun onProcessTypeChanged(processType: Int) {}

  /**
   * @see LocationListener.onStatusChanged
   */
  override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

  /**
   * @see LocationListener.onPermissionGranted
   */
  override fun onPermissionGranted(alreadyHadPermission: Boolean) = gpsPermissionGranted(alreadyHadPermission)

}
