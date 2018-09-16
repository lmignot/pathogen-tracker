package eu.mignot.pathogentracker.viewmodels

import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.human.AddHumanViewModel
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
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


  private val collectedOnDate = Date()

  private val mockSurveyRepo: SurveyRepository = spyk(RealmSurveysRepository)

  private val vm by lazy {
    AddHumanViewModel(
      AppFormDataProvider,
      mockSurveyRepo
    )
  }

  @Before
  fun init() {
    clearMocks(mockSurveyRepo)
  }

  @Test
  fun `save should call surveys repo`() {
    val survey = with(Human()) {
      this.id = TestAddVectorViewModel.SURVEY_ID
      this.collectedOn = collectedOnDate
      this
    }
    every { mockSurveyRepo.storeSurvey(survey) } just Runs
    vm.save(survey)
    verify(atLeast = 1) { mockSurveyRepo.storeSurvey(survey) }
  }

  @Test
  fun `should have the correct id prefix`() {
    assertEquals(vm.id.substring(0, 2), ID_PREFIX)
  }

  @Test
  fun `should provide todays date by default`() {
    val currentDate = Calendar.getInstance()
    assertEquals(vm.date.get(Calendar.YEAR), currentDate.get(Calendar.YEAR))
    assertEquals(vm.date.get(Calendar.MONTH), currentDate.get(Calendar.MONTH))
    assertEquals(vm.date.get(Calendar.DATE), currentDate.get(Calendar.DATE))
  }

  @Test
  fun `should allow user to change date`() {
    val compareDate = Calendar.getInstance()
    compareDate.set(TEST_YEAR, TEST_MONTH, TEST_DAY)
    vm.date = compareDate
    assertEquals(vm.date.get(Calendar.YEAR), TEST_YEAR)
    assertEquals(vm.date.get(Calendar.MONTH), TEST_MONTH)
    assertEquals(vm.date.get(Calendar.DATE), TEST_DAY)
  }

  @Test
  fun `should provide list of symptoms`() {
    val symptoms = vm.symptoms()
    assertEquals(symptoms.size, TEST_SYMPTOMS.size)
  }

}
