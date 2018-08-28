package eu.mignot.pathogentracker.syncservice

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.RealmSurveysRepository
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.util.PhotoRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info

class FirebasePhotoSyncService: JobService(), AnkoLogger {

  private val repository: SurveyRepository by lazy {
    App.getSurveysRepository()
  }

  private val photoRepository: PhotoRepository by lazy {
    App.getRemotePhotoRepository()
  }

  private lateinit var uploadQueue: List<Photo>

  override fun onStopJob(job: JobParameters?): Boolean {
    info { "Finished job: ${job.toString()}" }
    return false
  }

  override fun onStartJob(job: JobParameters?): Boolean {
    uploadQueue = repository.getPhotosToUpload()
    uploadQueue.forEach {
      doAsync {
        info { "Started upload task for photo ${it.fileName}"}
        photoRepository.storePhoto(it, false, null)
        jobFinished(job!!, false)
      }
    }
    return true
  }

}
