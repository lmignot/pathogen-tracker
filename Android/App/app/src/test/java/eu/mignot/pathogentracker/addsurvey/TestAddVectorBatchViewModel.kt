package eu.mignot.pathogentracker.addsurvey

import eu.mignot.pathogentracker.data.LocationProvider
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorBatchViewModel
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.VectorBatchSurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.ShouldSpec
import org.mockito.Mockito
import java.util.*

class TestAddVectorBatchViewModel: ShouldSpec() {

  companion object {
    const val ID_PREFIX = "VB-"
    const val TEST_YEAR = 2021
    const val TEST_MONTH = 6
    const val TEST_DAY = 10
  }

  init {

    val vm = AddVectorBatchViewModel(
      Mockito.mock(LocationProvider::class.java),
      eu.mignot.pathogentracker.surveys.data.VectorBatchSurveyRepository
    )

    should("generate an id starting with VB-") {
      vm.id.substring(0, 3) shouldBe ID_PREFIX
    }

    should("provide the current date by default") {
      val currentDate = Calendar.getInstance()
      val vmDate = vm.date
      vmDate.get(Calendar.YEAR) shouldBe currentDate.get(Calendar.YEAR)
      vmDate.get(Calendar.MONTH) shouldBe currentDate.get(Calendar.MONTH)
      vmDate.get(Calendar.DATE) shouldBe currentDate.get(Calendar.DATE)
    }

    should("allow user to change the date") {
      val compareDate = Calendar.getInstance()
      compareDate.set(TEST_YEAR, TEST_MONTH, TEST_DAY)
      vm.date = compareDate
      vm.date.get(Calendar.YEAR) shouldBe TEST_YEAR
      vm.date.get(Calendar.MONTH) shouldBe TEST_MONTH
      vm.date.get(Calendar.DATE) shouldBe TEST_DAY
    }

  }

}
