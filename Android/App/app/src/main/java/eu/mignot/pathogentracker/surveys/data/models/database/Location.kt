package eu.mignot.pathogentracker.surveys.data.models.database

import io.realm.RealmObject

open class Location(
  var longitude: Double = 0.0,
  var latitude: Double = 0.0,
  var accuracy: Float = -1f
): RealmObject() {
  override fun toString(): String = "Long: $longitude, Lat: $latitude"
}
