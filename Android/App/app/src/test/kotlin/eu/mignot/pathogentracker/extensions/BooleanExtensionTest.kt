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
  fun `as yes or no should return yes if true`() {
    assertEquals(yes.asYesOrNo(), YES)
  }

  @Test
  fun `as yes or no should return no if false`() {
    assertEquals(no.asYesOrNo(), NO)
  }

}
