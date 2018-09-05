package eu.mignot.pathogentracker.extensions

import eu.mignot.pathogentracker.util.asYesOrNo
import org.junit.Assert.assertEquals
import org.junit.Test

class BooleanExtensionTest {

  private val yes: Boolean = true
  private val no: Boolean = false

  companion object {
    const val YES = "Yes"
    const val NO = "No"
  }

  @Test
  fun as_yes_or_no_should_return_yes_if_true() {
    assertEquals(yes.asYesOrNo(), YES)
  }

  @Test
  fun as_yes_or_no_should_return_no_if_false() {
    assertEquals(no.asYesOrNo(), NO)
  }

}
