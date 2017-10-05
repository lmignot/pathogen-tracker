package eu.mignot.pathogentracker.data

import android.annotation.SuppressLint
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import eu.mignot.pathogentracker.surveys.data.models.ui.UiLocation
import io.reactivex.Observable

class AppLocationProvider(private val rxLocation: RxLocation): LocationProvider {

  private val locationRequest by lazy {
    LocationRequest
      .create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setNumUpdates(1)
  }

  @SuppressLint("MissingPermission")
  override fun getLocation(): Observable<UiLocation> {
    return rxLocation
      .settings()
      .checkAndHandleResolution(locationRequest)
      .flatMapObservable {
        rxLocation
          .location()
          .updates(locationRequest)
          .map({
            l ->
            UiLocation(l.longitude, l.latitude, l.accuracy)
          })
      }
  }

}
