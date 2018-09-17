package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.runner.AndroidJUnit4
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import java.io.File

@RunWith(AndroidJUnit4::class)
class RemotePhotoRepositoryTest {

  companion object {
    const val FILE_PARENT = "TEST_PARENT"
    const val FILE_NAME = "TEST_FILE_NAME"
    const val FILE_EXT = "TEST_FILE_EXT"
    const val ONE_MEGABYTE = 1024 * 1024 * 1L
  }

  @Mock
  private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

  @Mock
  private val temporaryFileProvider: TemporaryFileProvider = TemporaryFileProvider

  private val remoteRepo: PhotoRepository by lazy {
    FirebasePhotoRepository(storageRef)
  }

  private val localRepo: PhotoRepository by lazy {
    DevicePhotoRepository
  }

  private var tempJpeg: File? = temporaryFileProvider.getTempFile(FILE_NAME, FILE_EXT, App.getDeviceFileDir())
  private var bmp: Bitmap =
    Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)

  @Test
  fun remote_photo_repository_should_store_a_photo() {
    val photo: Photo = tempJpeg!!.let {
      with (Photo()) {
        fileName = it.name
        path = it.path
        parentId =
          FILE_PARENT
        this
      }
    }
    localRepo.storePhoto(photo, true, bmp)
    Thread.sleep(2000)
    remoteRepo.storePhoto(photo, false, null)
    Thread.sleep(5000)
    val instance = FirebaseStorage.getInstance().reference
    instance
      .child("${AppSettings.Constants.FIREBASE_PHOTO_PATH}/${photo.fileName}")
      .getBytes(ONE_MEGABYTE)
      .addOnCompleteListener {
        when (it.isSuccessful) {
          true -> {
            val bytes = it.result
            val dlBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Assert.assertEquals(bmp.byteCount, dlBmp.byteCount)
          }
          false -> fail()
        }

      }
  }

}
