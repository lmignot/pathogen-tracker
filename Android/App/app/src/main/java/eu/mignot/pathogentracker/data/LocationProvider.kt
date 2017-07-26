package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.data.models.Location
import io.reactivex.Single

interface LocationProvider {
  fun getLocation(): Single<Location>
}
