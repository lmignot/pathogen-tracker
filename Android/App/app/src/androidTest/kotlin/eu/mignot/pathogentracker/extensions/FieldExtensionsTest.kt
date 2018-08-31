package eu.mignot.pathogentracker.extensions

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.text.InputType
import android.widget.EditText
import eu.mignot.pathogentracker.util.asIntOrDefault
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FieldExtensionsTest {

  private var textInput: EditText = EditText(InstrumentationRegistry.getContext())

  companion object {
      const val TEST_POSITIVE_NUM: Int = 23
      const val TEST_NEGATIVE_NUM = -89
      const val TEST_DEFAULT_NUM = 98
      const val TEST_NOARGS_NUM = 0
  }

  @Before fun resetTextInput() {
    textInput.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
    textInput.setText("")
  }

  @Test
  fun should_return_default_value() {
    Assert.assertEquals(textInput.asIntOrDefault(TEST_DEFAULT_NUM), TEST_DEFAULT_NUM)
  }

  @Test
  fun should_return_positive_value() {
    textInput.setText(TEST_POSITIVE_NUM.toString())
    Assert.assertEquals(textInput.asIntOrDefault(TEST_DEFAULT_NUM), TEST_POSITIVE_NUM)
  }

  @Test
  fun should_return_negative_value() {
    textInput.setText(TEST_NEGATIVE_NUM.toString())
    Assert.assertEquals(textInput.asIntOrDefault(TEST_DEFAULT_NUM), TEST_NEGATIVE_NUM)
  }

  @Test
  fun should_return_zero_value_when_no_args() {
    Assert.assertEquals(textInput.asIntOrDefault(), TEST_NOARGS_NUM)
  }

}
