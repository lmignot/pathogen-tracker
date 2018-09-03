package eu.mignot.pathogentracker.data.models.remote

import java.util.*

data class RemoteVectorBatch(
  val id: String,
  val collectedOn: Date,
  val locationCollected: RemoteLocation?,
  val territory: String?,
  val temperature: Int? = 0,
  val weatherCondition: String?
)
