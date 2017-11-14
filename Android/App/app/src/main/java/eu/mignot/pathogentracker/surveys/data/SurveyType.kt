package eu.mignot.pathogentracker.surveys.data

import eu.mignot.pathogentracker.util.AppSettings

sealed class SurveyType {
  companion object {
    fun get(s: String): SurveyType = when (s) {
      AppSettings.Constants.VECTOR_TYPE_VALUE -> VECTOR()
      AppSettings.Constants.PATIENT_TYPE_VALUE -> PATIENT()
      else -> NONE
    }
  }

  class VECTOR : SurveyType() {
    override fun toString(): String = AppSettings.Constants.VECTOR_TYPE_VALUE
  }
  class PATIENT : SurveyType() {
    override fun toString(): String = AppSettings.Constants.PATIENT_TYPE_VALUE
  }
  object NONE : SurveyType()
}

