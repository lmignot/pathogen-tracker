package eu.mignot.pathogentracker.syncservice

import android.app.job.JobParameters
import android.app.job.JobService
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.repository.SurveyRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync

class FirebaseVectorSyncService: JobService(), AnkoLogger {

  private val localRepository: SurveyRepository by lazy {
    App.getLocalSurveysRepository()
  }

  private val remoteRepository: SurveyRepository by lazy {
    App.getRemoteSurveysRepository()
  }

  override fun onStopJob(job: JobParameters?): Boolean {
    return true
  }

  override fun onStartJob(job: JobParameters?): Boolean {
    val surveysToStore = localRepository.getSurveysToSync(Vector())

    when (surveysToStore.isEmpty()) {
      true -> jobFinished(job!!, true)
      false -> {
        surveysToStore.forEach {
          doAsync {
            remoteRepository.storeSurvey(it)
            if (localRepository.getSurveysToSync(Vector()).isEmpty()) {
              jobFinished(job!!, false)
            }
          }
        }
      }
    }

    return true
  }

}
