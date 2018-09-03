package eu.mignot.pathogentracker.data.models.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Photo: RealmObject() {
  @PrimaryKey
  @Required
  var fileName: String = ""

  @Required
  var path: String = ""

  @Required
  var parentId: String = ""

  var uploadedAt: Date? = null

  fun isUploaded(): Boolean = uploadedAt !== null
}
