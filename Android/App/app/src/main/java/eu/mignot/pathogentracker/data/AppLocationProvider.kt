package eu.mignot.pathogentracker.data

import android.annotation.SuppressLint
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import eu.mignot.pathogentracker.data.models.Location
import io.reactivex.Observable

class AppLocationProvider(private val rxLocation: RxLocation): LocationProvider {

  @SuppressLint("MissingPermission")
  override fun getLocation(): Observable<Location> {
    val locationRequest = LocationRequest
      .create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setNumUpdates(1)

    return rxLocation
      .settings()
      .checkAndHandleResolution(locationRequest)
      .flatMapObservable {
        rxLocation
          .location()
          .updates(locationRequest)
          .map({l -> Location(l.longitude, l.latitude, l.accuracy) })
      }
  }

}
