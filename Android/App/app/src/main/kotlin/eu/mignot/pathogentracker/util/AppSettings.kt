package eu.mignot.pathogentracker.util

/**
 * Provides constants used throughout PT app
 */
object AppSettings {

  object PreferenceKeys {
    const val PRIMARY_SURVEY_KEY = "PRIMARY_SURVEY_KEY"
    const val SECONDARY_SURVEY_KEY = "SECONDARY_SURVEY_KEY"
    const val USE_CELLULAR_KEY = "USE_CELLULAR_KEY"
    const val IMAGE_RESOLUTION_KEY = "IMAGE_RESOLUTION_KEY"
    const val ON_BOARDING_COMPLETE_KEY = "ON_BOARDING_COMPLETE_KEY"
    const val PI_NETWORK_ID_KEY = "PI_NETWORK_ID_KEY"
  }

  object Constants {
    const val ACTIVITY_CHOICE = "ACTIVITY_CHOICE"
    const val PERMISSION_CODE = "PERMISSION_CODE"
    const val PERMISSION_RATIONALE_ID = "PERMISSION_RATIONALE_ID"
    const val MESSAGE_KEY = "MESSAGE"
    const val NO_ID_VALUE = "NO_ID"
    const val NO_BATCH_ID_VALUE = "NO_BATCH_ID"
    const val SEQUENCE_ZERO_VALUE = 0
    const val SEQUENCE_INCREMENT_VALUE = 1
    const val BATCH_ID_KEY = "batchId"
    const val VECTOR_ID_KEY = "vectorId"
    const val VECTOR_SEQUENCE_KEY = "sequence"
    const val DEFAULT_IMAGE_QUALITY = 80
    const val IMAGE_EXTENSION = ".jpg"
    const val UNCOMPRESSED_IMAGE_QUALITY = 100
    const val DATE_FORMAT = "dd MMMM yyyy"
    const val DATE_TIME_FORMAT = "dd MMMM yyyy, HH:mm"
    const val PHOTO_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
    const val NO_VALUE = "None provided"
    const val MIN_TEMP = -40
    const val MAX_TEMP = 40
    const val VECTOR_TYPE_VALUE = "Mosquito"
    const val PATIENT_TYPE_VALUE = "Patient"
    const val NONE_TYPE_VALUE = "None"
    const val FIREBASE_PHOTO_PATH = "/photos/mosquitoes"
    const val HUMAN_COLLECTION = "patients"
    const val VECTOR_COLLECTION = "mosquitoes"
    const val VECTOR_BATCH_COLLECTION = "mosquito-batches"
    const val REALM_DB_NAME = "pt-local-db"
    const val REALM_SCHEMA_VERSION = 11L
    const val REPOSITORY_NI_RATIONALE = "Implement method when 2-way sync of surveys is in scope"
    const val JOB_ID_PHOTOS = 2948520
    const val JOB_ID_VECTORS = 2948521
    const val JOB_ID_VECTOR_BATCHES = 2948522
    const val JOB_ID_HUMANS = 2948523
    const val FILEPROVIDER_AUTHORITY = "eu.mignot.pathogentracker.fileprovider"
    const val SHOULD_AUTHENTICATE = "SHOULD_AUTH"
  }

  object RequestCodes {
    const val LOCATION_REQ_CODE = 1
    const val CAMERA_REQ_CODE = 2
    const val LOGIN_REQ_CODE = 3
  }
}
