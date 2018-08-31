package eu.mignot.pathogentracker.surveys.data.models.database

import io.realm.RealmList
import io.realm.RealmObject

open class Infection(
  var name: String = "",
  var symptoms: RealmList<Symptom> = RealmList()
): RealmObject() {
  override fun toString(): String =
    name + (if (symptoms.isNotEmpty()) ", Symptoms: " + symptoms.joinToString() else "")
}
