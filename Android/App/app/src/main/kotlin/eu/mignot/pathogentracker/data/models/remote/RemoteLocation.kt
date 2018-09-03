package eu.mignot.pathogentracker.data.models.remote

data class RemoteLocation(
  val longitude: Double,
  val latitude: Double,
  val accuracy: Float = -1f
)
