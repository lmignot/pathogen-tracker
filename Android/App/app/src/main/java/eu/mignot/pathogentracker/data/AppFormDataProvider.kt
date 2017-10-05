package eu.mignot.pathogentracker.data

object AppFormDataProvider: FormDataProvider {
  override fun diseases(): List<String> =
    listOf("Zika", "Dengue", "Malaria", "Other")

  override fun symptoms(): List<String> =
    listOf("Skin rash", "Fever", "Joint or muscle pain", "Vomiting", "Severe headache", "Other")
}
