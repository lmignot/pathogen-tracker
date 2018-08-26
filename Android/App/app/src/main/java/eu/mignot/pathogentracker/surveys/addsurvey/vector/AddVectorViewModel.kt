package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.graphics.Bitmap
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.util.AppSettings
import org.jetbrains.anko.AnkoLogger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddVectorViewModel (
  override val id: String,
  repository: SurveyRepository<Vector>,
  private val prefs: PreferencesProvider
): AnkoLogger, BaseViewModel<Vector>(repository) {

  private var photoPath: String? = null

  lateinit var photo: Bitmap

  fun getPhotoPath() = photoPath

  fun getTempImageFile(storageDir: File): File? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(Date())
    val fileName = "${id}_$timeStamp"
    return createTempFile(storageDir, fileName, ".jpg")
  }

  fun savePhoto(): Boolean {
    val imgQuality = if (prefs.getOptimizeImageRes()) {
      AppSettings.Constants.DEFAULT_IMAGE_QUALITY
    } else {
      AppSettings.Constants.UNCOMPRESSED_IMAGE_QUALITY
    }
    return when (photoPath) {
      null -> false
      else -> FileOutputStream(photoPath).use {
        photo.compress(Bitmap.CompressFormat.JPEG, imgQuality, it)
      }
    }
  }

  private fun createTempFile(storageDir: File, fileName: String, suffix: String): File? {
    return try {
      createTempFile(fileName, suffix, storageDir).let {
        photoPath = it.absolutePath
        it
      }
    } catch (e: IOException) {
      null
    }
  }

}
