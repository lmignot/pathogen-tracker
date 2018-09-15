package eu.mignot.pathogentracker.data

object AppFormDataProvider: FormDataProvider {

  /**
   * @see FormDataProvider.diseases
   */
  override fun diseases(): List<String> =
    listOf("Zika", "Dengue", "Malaria", "Other")

  /**
   * @see FormDataProvider.symptoms
   */
  override fun symptoms(): List<String> =
    listOf("Skin rash", "Fever", "Joint or muscle pain", "Vomiting", "Severe headache", "Other")
}
