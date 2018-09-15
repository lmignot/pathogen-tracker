package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.util.AppSettings

/**
 * ADT describing the types of surveys PT app
 * supports
 */
sealed class SurveyType {
  companion object {
    fun get(s: String): SurveyType = when (s) {
      AppSettings.Constants.VECTOR_TYPE_VALUE -> VECTOR
      AppSettings.Constants.PATIENT_TYPE_VALUE -> PATIENT
      else -> NONE
    }
  }

  object VECTOR : SurveyType() {
    override fun toString(): String = AppSettings.Constants.VECTOR_TYPE_VALUE
  }
  object PATIENT : SurveyType() {
    override fun toString(): String = AppSettings.Constants.PATIENT_TYPE_VALUE
  }
  object NONE : SurveyType()
}

