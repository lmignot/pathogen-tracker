package eu.mignot.pathogentracker.surveys.data.models.survey

import android.media.Image

data class Vector (
  val id: String,
  val batchId: String,
  val species: String,
  val gender: String,
  val stage: String,
  val didFeed: Boolean,
  val dna: String = "",
  val photo: Image
)
