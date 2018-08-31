package eu.mignot.pathogentracker.util

import android.graphics.Bitmap
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.util.AppSettings.Constants.PHOTO_TIMESTAMP_FORMAT
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object DevicePhotoRepository: PhotoRepository {

  private val storageDir by lazy {
    App.getDeviceFileDir()
  }

  override fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?) {
    val imgQuality = when (shouldOptimize) {
      true -> AppSettings.Constants.DEFAULT_IMAGE_QUALITY
      false -> AppSettings.Constants.UNCOMPRESSED_IMAGE_QUALITY
    }

    FileOutputStream(model.path).use {
      photo!!.compress(Bitmap.CompressFormat.JPEG, imgQuality, it)
    }
  }

  fun getTempImageFile(id: String): File? {
    val timeStamp = SimpleDateFormat(PHOTO_TIMESTAMP_FORMAT, Locale.UK).format(Date())
    val fileName = "${id}_$timeStamp"
    return getTempFile(storageDir, fileName, ".jpg")
  }

  private fun getTempFile(storageDir: File, fileName: String, suffix: String): File? {
    return try {
      createTempFile(fileName, suffix, storageDir).let {
        it
      }
    } catch (e: IOException) {
      null
    }
  }
}
