package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import android.support.test.runner.AndroidJUnit4
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File

@RunWith(AndroidJUnit4::class)
class RemotePhotoRepositoryTest {

  companion object {
      const val FILE_PARENT = "TEST_PARENT"
  }

  @Mock
  private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

  @Mock
  private val temporaryFileProvider: TemporaryFileProvider = TemporaryFileProvider

  private val remoteRepo: PhotoRepository by lazy {
    FirebasePhotoRepository(storageRef)
  }

  private lateinit var tempJpeg: File
  private lateinit var bmp: Bitmap

  @Before
  fun build_up() {
    MockitoAnnotations.initMocks(this)
    bmp = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)
//    tempJpeg =
  }

  @Test
  fun remote_should_store_a_photo() {
//    val photo = with (Photo()) {
//      fileName = tempJpeg.name
//      path = tempJpeg.path
//      parentId =
//        FILE_PARENT
//      this
//    }
  }

}
