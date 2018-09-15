package eu.mignot.pathogentracker.data.models.remote

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class RemoteVector(
  val id: String,
  val batchId: String,
  val sequence: Int,
  val species: String?,
  val gender: String?,
  val state: String?,
  val didFeed: Boolean,
  val photoId: String?,
  @ServerTimestamp
  val uploadedAt: Date? = null
)
