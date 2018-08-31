package eu.mignot.pathogentracker.syncservice

import android.app.job.JobParameters
import android.app.job.JobService
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.util.PhotoRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync

class FirebasePhotoSyncService: JobService(), AnkoLogger {

  private val repository: SurveyRepository by lazy {
    App.getLocalSurveysRepository()
  }

  private val photoRepository: PhotoRepository by lazy {
    App.getRemotePhotoRepository()
  }

  override fun onStopJob(job: JobParameters?): Boolean {
    return true
  }

  override fun onStartJob(job: JobParameters?): Boolean {
    val photosToUpload = repository.getSurveysToSync(Photo())
    if (photosToUpload.isEmpty()) {
      jobFinished(job!!, true)
    }

    when (photosToUpload.isEmpty()) {
      true -> jobFinished(job!!, true)
      false -> photosToUpload.forEach {
        doAsync {
          photoRepository.storePhoto(it, false, null)
          if (repository.getSurveysToSync(Photo()).isEmpty()) jobFinished(job!!, false)
        }
      }
    }

    return true
  }

}
