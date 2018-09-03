package eu.mignot.pathogentracker.data.models.remote

data class RemoteVector(
  val id: String,
  val batchId: String,
  val sequence: Int,
  val species: String?,
  val gender: String?,
  val state: String?,
  val didFeed: Boolean,
  val photoId: String?
)
