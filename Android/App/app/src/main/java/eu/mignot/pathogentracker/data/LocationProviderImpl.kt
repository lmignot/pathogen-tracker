package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.data.models.Location
import io.reactivex.Single

object LocationProviderImpl : LocationProvider {
  override fun getLocation(): Single<Location> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
