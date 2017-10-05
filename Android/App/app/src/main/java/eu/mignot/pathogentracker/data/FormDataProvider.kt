package eu.mignot.pathogentracker.data

interface FormDataProvider {
  fun diseases(): List<String>

  fun symptoms(): List<String>
}
