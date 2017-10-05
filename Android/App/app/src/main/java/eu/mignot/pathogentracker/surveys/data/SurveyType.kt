package eu.mignot.pathogentracker.surveys.data

import eu.mignot.pathogentracker.util.AppSettings

enum class SurveyType(val value: String) {
  VECTOR(AppSettings.Constants.VECTOR_TYPE_VALUE),
  PATIENT(AppSettings.Constants.PATIENT_TYPE_VALUE),
  NONE(AppSettings.Constants.NONE_TYPE_VALUE);

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
//data class VECTOR(val name: String = AppSettings.Constants.VECTOR_TYPE_VALUE): SType() {
//  override fun toString(): String = name
//}
//data class PATIENT(val name: String = AppSettings.Constants.PATIENT_TYPE_VALUE): SType() {
//  override fun toString(): String = name
//}
//object NONE: SType()
