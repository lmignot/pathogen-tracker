package eu.mignot.pathogentracker.data.models

data class User (
  val userId: Long,
  val name: String,
  val email: String,
  val token: String
)
