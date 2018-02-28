package eu.mignot.pathogentracker.extensions.suite

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.text.InputType
import android.widget.EditText
import eu.mignot.pathogentracker.util.asIntOrDefault
import io.kotlintest.TestCase
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.ShouldSpec
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FieldExtensionsTest: ShouldSpec() {

  private var textInput: EditText = EditText(InstrumentationRegistry.getContext())

  companion object {
      const val TEST_POSITIVE_NUM = 23
      const val TEST_NEGATIVE_NUM = -89
      const val TEST_DEFAULT_NUM = 0
  }

  init {
    textInput.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED

    @Test
    fun defValue(): TestCase {
      return should("return a default value") {
        textInput.asIntOrDefault(TEST_DEFAULT_NUM) shouldBe TEST_DEFAULT_NUM
      }
    }

    @Test
    fun posValue(): TestCase {
      return should("return a positive value") {
        textInput.setText(TEST_POSITIVE_NUM.toString())
        textInput.asIntOrDefault(TEST_DEFAULT_NUM) shouldBe TEST_POSITIVE_NUM
      }
    }

    @Test
    fun negValue(): TestCase {
      return should("return a negative value") {
        textInput.setText(TEST_NEGATIVE_NUM.toString())
        textInput.asIntOrDefault(TEST_DEFAULT_NUM) shouldBe TEST_NEGATIVE_NUM
      }
    }
  }

}
