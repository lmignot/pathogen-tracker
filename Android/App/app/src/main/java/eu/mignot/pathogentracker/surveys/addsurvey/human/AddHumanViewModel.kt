package eu.mignot.pathogentracker.surveys.addsurvey.human

import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.surveys.data.models.ui.CurrentDisease
import eu.mignot.pathogentracker.surveys.data.models.ui.UiLocation
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.surveys.addsurvey.CurrentDateProvider
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import io.reactivex.Observable
import org.jetbrains.anko.AnkoLogger
import java.util.*

class AddHumanViewModel(
  private val locationProvider: LocationProvider,
  private val formDataProvider: FormDataProvider,
  repository: SurveyRepository<Human>
): AnkoLogger, BaseViewModel<Human>(repository), CurrentDateProvider {

  companion object {
    const val PREFIX = "H"
  }

  override val id: String by lazy { "$PREFIX-${UUID.randomUUID()}" }

  override val currentDate: Calendar by lazy { Calendar.getInstance() }

  fun getLocation(): Observable<UiLocation> = locationProvider.getLocation()

  fun symptoms() = formDataProvider.symptoms()

  var currentDiseases: List<CurrentDisease> = listOf()

  var isPregnant: Boolean = false

  var gender: String = ""

  var location: UiLocation? = null

  fun deleteCurrentDisease(id: String) {
    currentDiseases = currentDiseases.filter { it.id != id }
  }

  fun addCurrentDisease(d: CurrentDisease) {
    currentDiseases = currentDiseases.plus(d)
  }

  private val currentDateOfBirth by lazy { Calendar.getInstance() }

  var dateOfBirth: Calendar
    get() = currentDateOfBirth
    set(d) = setCurrentDateOfBirth(d)

  private fun setCurrentDateOfBirth(d: Calendar?) {
    d?.let {
      currentDateOfBirth.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DATE))
    }
  }

}
