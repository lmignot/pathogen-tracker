package eu.mignot.pathogentracker.surveys.addsurvey.vector

import io.reactivex.Observable
import java.util.*
import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.surveys.data.models.ui.UiLocation
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.surveys.addsurvey.CurrentDateProvider
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import org.jetbrains.anko.AnkoLogger

class AddVectorBatchViewModel(
  private val locationProvider: LocationProvider,
  surveyRepository: SurveyRepository<VectorBatch>
): AnkoLogger, BaseViewModel<VectorBatch>(surveyRepository), CurrentDateProvider {

  companion object {
      const val PREFIX = "VB"
  }

  override val id: String by lazy { "$PREFIX-${UUID.randomUUID()}" }

  override val currentDate: Calendar by lazy { Calendar.getInstance() }

  var location: UiLocation? = null

  var territory: String? = null

  fun getLocation(): Observable<UiLocation> = locationProvider.getLocation()

}
