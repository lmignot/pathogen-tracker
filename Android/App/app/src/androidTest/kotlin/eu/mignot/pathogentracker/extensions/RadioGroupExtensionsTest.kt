package eu.mignot.pathogentracker.extensions

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import eu.mignot.pathogentracker.util.asBoolean
import eu.mignot.pathogentracker.util.selectedValue
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RadioGroupExtensionsTest {

  companion object {
    const val YES = "Yes"
    const val NO = "No"
    const val VALUE_0 = "Tree"
    const val VALUE_1 = "Mountain"
    const val VALUE_2 = "Sky"
    const val EMPTY_STRING = ""
  }

  private val yesNoGroup = with(AnkoContext.create(InstrumentationRegistry.getContext())) {
    radioGroup {
      radioButton {
        text = YES
      }
      radioButton {
        text = NO
      }
    }
  }
  private val radioGroup = with(AnkoContext.create(InstrumentationRegistry.getContext())) {
    radioGroup {
      radioButton {
        text = VALUE_0
      }
      radioButton {
        text = VALUE_1
      }
      radioButton {
        text = VALUE_2
      }
    }
  }

  @Before fun prepare() {
    radioGroup.clearCheck()
    yesNoGroup.clearCheck()
  }

  @Test
  fun selected_value_should_return_value_of_selected_radio() {
    radioGroup.check(radioGroup.getChildAt(0).id)
    assertEquals(radioGroup.selectedValue(), VALUE_0)
    radioGroup.check(radioGroup.getChildAt(1).id)
    assertEquals(radioGroup.selectedValue(), VALUE_1)
    radioGroup.check(radioGroup.getChildAt(2).id)
    assertEquals(radioGroup.selectedValue(), VALUE_2)
  }

  @Test
  fun selected_value_should_return_empty_string_if_none_selected() {
    assertEquals(radioGroup.selectedValue(), EMPTY_STRING)
  }

  @Test
  fun as_boolean_should_return_yes_as_true() {
    yesNoGroup.check(yesNoGroup.getChildAt(0).id)
    assertTrue(yesNoGroup.asBoolean())
  }

  @Test
  fun as_boolean_should_return_no_as_false() {
    yesNoGroup.check(yesNoGroup.getChildAt(1).id)
    assertFalse(yesNoGroup.asBoolean())
  }

  @Test
  fun as_boolean_should_return_anything_other_than_yes_as_false() {
    radioGroup.check(radioGroup.getChildAt(0).id)
    assertFalse(radioGroup.asBoolean())
  }

}
