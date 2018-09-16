package eu.mignot.pathogentracker.preferences

import android.support.test.runner.AndroidJUnit4
import eu.mignot.pathogentracker.TestCommon.signIn
import eu.mignot.pathogentracker.data.SurveyType
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class UserCanChangePreferencesTest {

  private val prefsProvider = AppPreferencesProvider

  @Before
  fun startUp() {
    signIn()
    prefsProvider.setPrimarySurveyActivity(SurveyType.PATIENT)
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
  }

  @Test
  fun cellular_transmission_can_be_enabled() {
    prefsProvider.setUseCellular(true)
    assertTrue(prefsProvider.getUseCellular())
  }

  @Test
  fun cellular_transmission_can_be_disabled() {
    prefsProvider.setUseCellular(false)
    assertFalse(prefsProvider.getUseCellular())
  }

  @Test
  fun image_optimization_can_be_enabled() {
    prefsProvider.setOptimizeImageRes(true)
    assertTrue(prefsProvider.getOptimizeImageRes())
  }

  @Test
  fun image_optimization_can_be_disabled() {
    prefsProvider.setOptimizeImageRes(false)
    assertFalse(prefsProvider.getOptimizeImageRes())
  }

  @Test
  fun primary_survey_can_be_changed() {
    assertTrue(prefsProvider.setPrimarySurveyActivity(SurveyType.VECTOR))
  }

  @Test
  fun secondary_survey_can_be_changed() {
    prefsProvider.setPrimarySurveyActivity(SurveyType.VECTOR)
    assertTrue(prefsProvider.setSecondarySurveyActivity(SurveyType.PATIENT))
  }

  @Test
  fun secondary_survey_can_be_removed() {
    assertTrue(prefsProvider.setSecondarySurveyActivity(SurveyType.NONE))
  }

  @Test
  fun primary_survey_cannot_be_removed() {
    assertFalse(prefsProvider.setPrimarySurveyActivity(SurveyType.NONE))
  }

  @Test
  fun secondary_survey_warn_if_same_as_primary() {
    // reset
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
    prefsProvider.setPrimarySurveyActivity(SurveyType.VECTOR)

    assertFalse(prefsProvider.setSecondarySurveyActivity(SurveyType.VECTOR))

    // reset
    prefsProvider.setSecondarySurveyActivity(SurveyType.NONE)
    prefsProvider.setPrimarySurveyActivity(SurveyType.PATIENT)

    assertFalse(prefsProvider.setSecondarySurveyActivity(SurveyType.PATIENT))
  }

}
