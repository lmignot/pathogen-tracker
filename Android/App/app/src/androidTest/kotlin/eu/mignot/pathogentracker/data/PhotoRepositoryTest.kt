package eu.mignot.pathogentracker.data

import android.graphics.Bitmap
import android.support.test.runner.AndroidJUnit4
import com.google.firebase.storage.FirebaseStorage
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.repository.DevicePhotoRepository
import eu.mignot.pathogentracker.repository.FirebasePhotoRepository
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File

@RunWith(AndroidJUnit4::class)
class PhotoRepositoryTest {

  companion object {
      const val FILE_ID = "TEST_IMAGE"
      const val EXT_JPG = ".jpg"
      const val FILE_PARENT = "TEST_PARENT"
  }

  private val fileProvider by lazy {
    TemporaryFileProvider
  }

  private val localRepo by lazy {
    DevicePhotoRepository
  }

  @Mock
  private val storageRef = FirebaseStorage.getInstance().reference

  private val remoteRepo by lazy {
    FirebasePhotoRepository(storageRef)
  }

  private lateinit var tempJpeg: File
  private lateinit var bmp: Bitmap

  @Before
  fun build_up() {
    MockitoAnnotations.initMocks(this);
    tempJpeg = fileProvider.getTempFile(FILE_ID, EXT_JPG)!!
    bmp = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)
  }

  @Test
  fun local_should_store_and_retrieve_a_photo() {
    val photo = with (Photo()) {
      fileName = tempJpeg.name
      path = tempJpeg.path
      parentId = FILE_PARENT
      this
    }
    doAsync {
      localRepo.storePhoto(photo, true, bmp)
      uiThread {
        val storedPhoto = localRepo.retrievePhoto(photo.path)!!
        assertEquals(storedPhoto.byteCount, bmp.byteCount)
        assertEquals(storedPhoto.height, bmp.height)
        assertEquals(storedPhoto.width, bmp.width)
      }
    }
  }

  @Test
  fun local_should_not_store_a_null_photo() {
    val photo = with (Photo()) {
      fileName = tempJpeg.name
      path = tempJpeg.path
      parentId = FILE_PARENT
      this
    }
    doAsync {
      localRepo.storePhoto(photo, true, null)
      uiThread {
        val storedPhoto = localRepo.retrievePhoto(photo.path)
        assertNull(storedPhoto)
      }
    }
  }

}
