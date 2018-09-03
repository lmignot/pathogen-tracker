package eu.mignot.pathogentracker.data.models.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class VectorBatch: RealmObject() {
  @PrimaryKey
  @Required
  var id: String = ""

  @Required
  var collectedOn: Date = Date()

  var locationCollected: Location? = null

  var territory: String? = ""

  var temperature: Int? = 0

  var weatherCondition: String? = ""

  var uploadedAt: Date? = null

  fun isUploaded(): Boolean = uploadedAt !== null

}
