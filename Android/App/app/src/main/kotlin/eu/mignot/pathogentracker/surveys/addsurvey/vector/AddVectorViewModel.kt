package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.graphics.Bitmap
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.repository.DevicePhotoRepository
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import org.jetbrains.anko.AnkoLogger
import java.io.File

class AddVectorViewModel (
  override val id: String,
  repository: SurveyRepository,
  private val prefs: PreferencesProvider,
  private val photoRepository: DevicePhotoRepository
): AnkoLogger, BaseViewModel<Vector>(repository) {

  var photo: Bitmap? = null

  private var photoPath: String? = null

  fun getPhotoPath() = photoPath

  fun getTempImageFile(): File? {
    return photoRepository.getTempImageFile(id)?.let {
      photoPath = it.absolutePath
      it
    }
  }

  fun savePhoto(model: Photo) =
    photoRepository.storePhoto(model, prefs.getOptimizeImageRes(), photo)

}
