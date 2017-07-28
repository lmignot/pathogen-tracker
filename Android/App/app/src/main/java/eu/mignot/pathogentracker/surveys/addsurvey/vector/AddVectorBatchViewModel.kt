package eu.mignot.pathogentracker.surveys.addsurvey.vector

import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.data.models.Location
import eu.mignot.pathogentracker.surveys.data.models.survey.VectorBatch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AddVectorBatchViewModel(
  val locationProvider: LocationProvider
): AnkoLogger {

  private val currentDate by lazy { Calendar.getInstance() }

  val id by lazy { "VB-${UUID.randomUUID()}" }

  var date: Calendar
    get() = currentDate
    set(d) = setCurrentDate(d)

  fun getLocation(): Observable<Location> = locationProvider.getLocation()

  fun save(model: VectorBatch): Single<Boolean> {
    info {model.toString()}
    return Single.just(true)
  }

  private fun setCurrentDate(d: Calendar): Unit {
    currentDate.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DATE))
  }

}
