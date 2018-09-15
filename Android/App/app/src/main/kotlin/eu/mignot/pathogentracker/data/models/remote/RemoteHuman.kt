package eu.mignot.pathogentracker.data.models.remote

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class RemoteHuman(
  val id: String,
  val collectedOn: Date,
  val locationCollected: RemoteLocation?,
  val dateOfBirth: Date?,
  val gender: String?,
  val isPregnant: Boolean,
  val isFamilyMemberIll: Boolean,
  val isFlagged: Boolean,
  val samples: List<String>,
  val travelHistory: List<String>,
  val pastInfections: List<String>,
  val currentInfections: List<RemoteInfection>,
  @ServerTimestamp
  val uploadedAt: Date? = null
)
