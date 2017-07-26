package eu.mignot.pathogentracker.surveys.addsurvey.vector

import io.reactivex.Single
import java.util.*
import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.data.models.Location

class AddVectorBatchViewModel(
  val locationProvider: LocationProvider
) {

  private val currentDate by lazy { Calendar.getInstance() }

  val id by lazy { "VB-${UUID.randomUUID()}" }

  var date: Calendar
    get() = currentDate
    set(d) = setCurrentDate(d)

  fun getLocation(): Single<Location> = locationProvider.getLocation()

  fun save(): Single<Boolean> {
    TODO("NI")
  }

  private fun setCurrentDate(d: Calendar): Unit {
    currentDate.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DATE))
  }

}
