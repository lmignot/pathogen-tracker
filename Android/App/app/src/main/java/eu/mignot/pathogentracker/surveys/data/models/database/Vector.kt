package eu.mignot.pathogentracker.surveys.data.models.database

import eu.mignot.pathogentracker.util.AppSettings
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Vector: RealmObject() {
  @PrimaryKey @Required var id: String = ""

  @Index @Required var batchId: String = ""

  @Index var sequence: Int = 0

  var species: String = ""

  var gender: String = ""

  var stage: String = ""

  var didFeed: Boolean = false

  var dna: String = ""

  var photoPath: String? = null

  var uploadedAt: Date? = null

  fun isUploaded(): Boolean = uploadedAt !== null
}

/**
 * Extension function, retrieve the next sequence ID for a [Vector]
 * assigned to a [VectorBatch]
 */
fun List<Vector>.getNextVectorSequence(): Int {
  return (this.maxBy { it.sequence }?.sequence ?: AppSettings.Constants.SEQUENCE_ZERO_VALUE) +
    AppSettings.Constants.SEQUENCE_INCREMENT_VALUE
}
