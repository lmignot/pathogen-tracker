package eu.mignot.pathogentracker.addsurvey

import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchViewModel
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestAddVectorBatchViewModel {

  companion object {
    const val ID_PREFIX = "VB-"
    const val TEST_YEAR = 2021
    const val TEST_MONTH = 6
    const val TEST_DAY = 10
  }

  private val vm by lazy {
    AddVectorBatchViewModel(RealmSurveysRepository)
  }

  @Test
  fun should_generate_id_prefix() {
    assertEquals(vm.id.substring(0, 3), ID_PREFIX)
  }

  @Test
  fun should_provide_current_date_by_default() {
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
    assertEquals(vm.date.get(Calendar.YEAR), compareDate.get(Calendar.YEAR))
    assertEquals(vm.date.get(Calendar.MONTH), compareDate.get(Calendar.MONTH))
    assertEquals(vm.date.get(Calendar.DATE), compareDate.get(Calendar.DATE))
  }

}
