package eu.mignot.pathogentracker.surveys.addsurvey.human

import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.ui.CurrentDisease
import eu.mignot.pathogentracker.data.models.ui.UiLocation
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.surveys.addsurvey.CurrentDateProvider
import org.jetbrains.anko.AnkoLogger
import java.util.*

class AddHumanViewModel(
  private val formDataProvider: FormDataProvider,
  repository: SurveyRepository
): AnkoLogger, BaseViewModel<Human>(repository), CurrentDateProvider {

  companion object {
    const val PREFIX = "H"
  }

  override val id: String by lazy { "$PREFIX-${UUID.randomUUID()}" }

  override val currentDate: Calendar by lazy { Calendar.getInstance() }

  /**
   * @see FormDataProvider.symptoms
   */
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
