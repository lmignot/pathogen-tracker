package eu.mignot.pathogentracker.surveys.data.models.database

import io.realm.RealmObject

open class Symptom(var name: String = ""): RealmObject() {
  override fun toString(): String = name
}
