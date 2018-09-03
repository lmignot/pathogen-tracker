package eu.mignot.pathogentracker.data.models.remote

data class RemoteInfection(
  val name: String,
  val symptoms: List<String>
)
