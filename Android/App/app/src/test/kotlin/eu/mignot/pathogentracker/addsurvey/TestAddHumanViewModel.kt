package eu.mignot.pathogentracker.addsurvey

import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestAddHumanViewModel {

  companion object {
    const val ID_PREFIX = "H-"
    const val TEST_YEAR = 2021
    const val TEST_MONTH = 6
    const val TEST_DAY = 10
    val TEST_SYMPTOMS = listOf("Skin rash", "Fever", "Joint or muscle pain", "Vomiting", "Severe headache", "Other")
  }

  private val vm by lazy {
    AddHumanViewModel(
      AppFormDataProvider,
      RealmSurveysRepository
    )
  }

  @Test
  fun should_have_the_correct_id_prefix() {
    assertEquals(vm.id.substring(0, 2), ID_PREFIX)
  }

  @Test
  fun should_provide_todays_date_by_default() {
    val currentDate = Calendar.getInstance()
    assertEquals(vm.date.get(Calendar.YEAR), currentDate.get(Calendar.YEAR))
    assertEquals(vm.date.get(Calendar.MONTH), currentDate.get(Calendar.MONTH))
    assertEquals(vm.date.get(Calendar.DATE), currentDate.get(Calendar.DATE))
  }

  @Test
  fun should_allow_user_to_change_date() {
    val compareDate = Calendar.getInstance()
    compareDate.set(TEST_YEAR, TEST_MONTH, TEST_DAY)
    vm.date = compareDate
    assertEquals(vm.date.get(Calendar.YEAR), TEST_YEAR)
    assertEquals(vm.date.get(Calendar.MONTH), TEST_MONTH)
    assertEquals(vm.date.get(Calendar.DATE), TEST_DAY)
  }

  @Test
  fun should_provide_list_of_symptoms() {
    val symptoms = vm.symptoms()
    assertEquals(symptoms.size, TEST_SYMPTOMS.size)
  }

}
