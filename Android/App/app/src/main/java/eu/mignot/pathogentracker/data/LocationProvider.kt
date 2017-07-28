package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.data.models.Location
import io.reactivex.Observable

interface LocationProvider {
  fun getLocation(): Observable<Location>
}
