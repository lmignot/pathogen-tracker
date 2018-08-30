package eu.mignot.pathogentracker.syncservice

import android.app.job.JobParameters
import android.app.job.JobService
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import org.jetbrains.anko.AnkoLogger

class FirebaseDataSyncService: JobService(), AnkoLogger {

  private val repository: SurveyRepository by lazy {
    App.getSurveysRepository()
  }


  override fun onStopJob(job: JobParameters?): Boolean {
    return false
  }

  override fun onStartJob(job: JobParameters?): Boolean {
    return false
  }

}
