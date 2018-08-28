package eu.mignot.pathogentracker.syncservice

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import io.realm.RealmObject

class FirebaseDataSyncService: JobService() {

  private lateinit var uploadQueue: List<RealmObject>

  /*
   * 1. Return if upload queue is empty
   */
  override fun onStopJob(job: JobParameters?): Boolean {

    return uploadQueue.isNotEmpty() // Answers the question: "Is there still work going on?"
  }

  /*
   * 1. get all Human, VectorBatch, Vector items where isUploaded = false
   * 2. if empty, return false
   * 3. if not empty prep items and add to queue
   * 4. trigger upload
   */
  override fun onStartJob(job: JobParameters?): Boolean {

    return false // Answers the question: "Should this job be retried?"
  }

}
