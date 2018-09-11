package eu.mignot.pathogentracker.util

import android.support.test.runner.AndroidJUnit4
import eu.mignot.pathogentracker.App
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TemporaryFileProviderTest {

  companion object {
      const val FILE_ID = "MY_TEST_FILE"
      const val FILE_EXT = ".jpg"
  }

  private val storageDir by lazy {
    App.getDeviceFileDir()
  }

  private val provider by lazy {
    App.getTempFileProvider()
  }

  @Test
  fun file_should_not_be_null() {
    val file = provider.getTempFile(FILE_ID, FILE_EXT)
    assertNotNull(file)
  }

  @Test
  fun name_should_begin_with_prefix() {
    val file = provider.getTempFile(FILE_ID, FILE_EXT)
    val prefix = file!!.name.substring(0, FILE_ID.length)
    assertEquals(prefix, FILE_ID)
  }

  @Test
  fun path_should_be_in_app_storage() {
    val file = provider.getTempFile(FILE_ID, FILE_EXT)
    assertEquals(storageDir.toString(), file!!.parent)
  }

}
