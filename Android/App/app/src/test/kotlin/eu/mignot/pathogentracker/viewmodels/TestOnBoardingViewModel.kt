package eu.mignot.pathogentracker.viewmodels

import eu.mignot.pathogentracker.data.SurveyType
import eu.mignot.pathogentracker.onboarding.OnBoardingViewModel
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import io.mockk.*
import org.junit.Before
import org.junit.Test

class TestOnBoardingViewModel {

  private val prefsProvider: PreferencesProvider = spyk(AppPreferencesProvider)


  private val vm by lazy {
    OnBoardingViewModel(prefsProvider)
  }

  @Before
  fun init() {
    clearMocks(prefsProvider)
  }

  @Test
  fun `should set primary survey as patient`() {
    val sType = SurveyType.PATIENT
    every { prefsProvider.setPrimarySurveyActivity(sType) } returns true
    vm.primaryActivity = sType
    verify(exactly = 1) { prefsProvider.setPrimarySurveyActivity(sType) }
  }

  @Test
  fun `should set primary survey as vector`() {
    val sType = SurveyType.VECTOR
    every { prefsProvider.setPrimarySurveyActivity(sType) } returns true
    vm.primaryActivity = sType
    verify(exactly = 1) { prefsProvider.setPrimarySurveyActivity(sType) }
  }

  @Test
  fun `should set secondary survey as patient`() {
    val sType = SurveyType.PATIENT
    every { prefsProvider.setPrimarySurveyActivity(sType) } returns true
    vm.primaryActivity = sType
    verify(exactly = 1) { prefsProvider.setPrimarySurveyActivity(sType) }
  }

  @Test
  fun `should set secondary survey as vector`() {
    val sType = SurveyType.VECTOR
    every { prefsProvider.setPrimarySurveyActivity(sType) } returns true
    vm.primaryActivity = sType
    verify(exactly = 1) { prefsProvider.setPrimarySurveyActivity(sType) }
  }

  @Test
  fun `should set secondary survey as none`() {
    val sType = SurveyType.NONE
    every { prefsProvider.setPrimarySurveyActivity(sType) } returns true
    vm.primaryActivity = sType
    verify(exactly = 1) { prefsProvider.setPrimarySurveyActivity(sType) }
  }

  @Test
  fun `should set complete = true`() {
    every { prefsProvider.setDidCompleteOnBoarding(true) } just Runs
    vm.setOnBoardingComplete(true)
    verify(exactly = 1) { prefsProvider.setDidCompleteOnBoarding(true) }
  }

  @Test
  fun `should set complete = false`() {
    every { prefsProvider.setDidCompleteOnBoarding(false) } just Runs
    vm.setOnBoardingComplete(false)
    verify(exactly = 1) { prefsProvider.setDidCompleteOnBoarding(false) }
  }

}
