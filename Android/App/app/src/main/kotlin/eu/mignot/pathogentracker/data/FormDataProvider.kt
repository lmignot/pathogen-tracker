package eu.mignot.pathogentracker.data

interface FormDataProvider {
  /**
   * Return a list of disease values
   */
  fun diseases(): List<String>

  /**
   * Return a list of disease symptoms
   */
  fun symptoms(): List<String>
}
