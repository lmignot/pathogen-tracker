package eu.mignot.pathogentracker.surveys.data.models.survey

import android.graphics.Bitmap

data class Vector (
  val id: String,
  val batchId: String,
  val species: String,
  val gender: String,
  val stage: String,
  val didFeed: Boolean,
  val dna: String = "",
  val photo: Bitmap
)
