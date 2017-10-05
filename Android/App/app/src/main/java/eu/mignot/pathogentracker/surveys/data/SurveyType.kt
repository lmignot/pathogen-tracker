package eu.mignot.pathogentracker.surveys.data

enum class SurveyType(val value: String) {
  VECTOR("Mosquito"),
  PATIENT("Patient"),
  NONE("None");

  override fun toString(): String {
    return value
  }

  companion object {
    fun get(s: String): SurveyType = when(s) {
      SurveyType.PATIENT.value -> SurveyType.PATIENT
      SurveyType.VECTOR.value -> SurveyType.VECTOR
      else -> SurveyType.NONE
    }
  }
}

// TODO: switch SurveyType for this, better than enum
//sealed class SType
//data class VECTOR(val name: String = "Mosquito"): SType() {
//  override fun toString(): String = name
//}
//data class PATIENT(val name: String = "Patient"): SType() {
//  override fun toString(): String = name
//}
//object NONE: SType()
