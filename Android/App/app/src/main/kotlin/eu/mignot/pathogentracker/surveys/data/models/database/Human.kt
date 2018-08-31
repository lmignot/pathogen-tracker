package eu.mignot.pathogentracker.surveys.data.models.database

import java.util.*
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Human: RealmObject() {
  @PrimaryKey
  @Required
  var id: String = ""

  @Required
  var collectedOn: Date = Date()

  var locationCollected: Location? = null

  var dateOfBirth: Date? = null

  var gender: String? = ""

  var isPregnant: Boolean = false

  var isFamilyMemberIll: Boolean = false

  var isFlagged: Boolean = false

  var samples: RealmList<SampleType> = RealmList()

  var travelHistory: RealmList<Country> = RealmList()

  var pastInfections: RealmList<Infection> = RealmList()

  var currentInfections: RealmList<Infection> = RealmList()

  var uploadedAt: Date? = null

  fun isUploaded(): Boolean = uploadedAt !== null
}
