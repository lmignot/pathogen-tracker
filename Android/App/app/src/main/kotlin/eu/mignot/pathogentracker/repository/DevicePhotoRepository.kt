package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.util.AppSettings
import java.io.FileOutputStream

object DevicePhotoRepository: PhotoRepository {

  /**
   * @see PhotoRepository.storePhoto
   */
  override fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?) {
    photo?.let {bmp ->
      val imgQuality = when (shouldOptimize) {
        true -> AppSettings.Constants.DEFAULT_IMAGE_QUALITY
        false -> AppSettings.Constants.UNCOMPRESSED_IMAGE_QUALITY
      }

      FileOutputStream(model.path).use {
        bmp.compress(Bitmap.CompressFormat.JPEG, imgQuality, it)
      }
    }
  }

  /**
   * @see PhotoRepository.retrievePhoto
   */
  override fun retrievePhoto(path: String): Bitmap? = BitmapFactory.decodeFile(path)
}
