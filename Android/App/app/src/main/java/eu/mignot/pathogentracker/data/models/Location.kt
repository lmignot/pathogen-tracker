package eu.mignot.pathogentracker.data.models

data class Location (
  val longitude: Double = 0.0,
  val latitude: Double = 0.0
) {
  override fun toString(): String = "lat: $latitude, long: $longitude"
}
