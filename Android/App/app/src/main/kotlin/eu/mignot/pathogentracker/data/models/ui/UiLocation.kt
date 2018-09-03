package eu.mignot.pathogentracker.data.models.ui

data class UiLocation(
  val longitude: Double = 0.0,
  val latitude: Double = 0.0,
  val accuracy: Float = -1f
) {
  override fun toString(): String = "Lat: $latitude, Long: $longitude"
}
