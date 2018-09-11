package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.graphics.Bitmap
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.repository.PhotoRepository
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import org.jetbrains.anko.AnkoLogger
import java.io.File

class AddVectorViewModel (
  override val id: String,
  repository: SurveyRepository,
  private val prefs: PreferencesProvider,
  private val photoRepository: PhotoRepository,
  private val tempFileProvider: TemporaryFileProvider
): AnkoLogger, BaseViewModel<Vector>(repository) {

  var photo: Bitmap? = null

  private var photoPath: String? = null

  fun getPhotoPath() = photoPath

  fun getTempImageFile(): File? {
    return tempFileProvider.getTempFile(id, AppSettings.Constants.IMAGE_EXTENSION)?.let {
      photoPath = it.absolutePath
      it
    }
  }

  fun savePhoto(model: Photo) =
    photoRepository.storePhoto(model, prefs.getOptimizeImageRes(), photo)

}
