package eu.mignot.pathogentracker.data.models.database

import io.realm.RealmObject

open class Country(var name: String = ""): RealmObject() {
  override fun toString(): String = name
}
