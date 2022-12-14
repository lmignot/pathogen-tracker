package eu.mignot.pathogentracker

import android.app.Application
import android.os.Environment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import eu.mignot.pathogentracker.auth.FirebaseLoginProvider
import eu.mignot.pathogentracker.auth.LoginProvider
import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.repository.*
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File

class App : Application() {

  private val deviceFileDir: File by lazy {
    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  }

  private val tempFileProvider: TemporaryFileProvider by lazy {
    TemporaryFileProvider
  }

  private val prefsProvider: PreferencesProvider by lazy {
    AppPreferencesProvider
  }

  private val formDataProvider: FormDataProvider by lazy {
    AppFormDataProvider
  }

  private val localSurveysRepository: SurveyRepository by lazy {
    // configure Realm instance
    val config = RealmConfiguration
      .Builder()
      .name(AppSettings.Constants.REALM_DB_NAME)
      .schemaVersion(AppSettings.Constants.REALM_SCHEMA_VERSION)
      .deleteRealmIfMigrationNeeded()
      .build()
    RealmSurveysRepository(config)
  }

  private val fireStoreInstance: FirebaseFirestore by lazy {
    val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(false)
      .setTimestampsInSnapshotsEnabled(true)
      .build()
    val db = FirebaseFirestore.getInstance()
    db.firestoreSettings = settings
    db
  }

  private val remoteSurveysRepository: SurveyRepository by lazy {
    FirebaseSurveysRepository(
      fireStoreInstance
    )
  }

  private val authProvider: LoginProvider by lazy {
    FirebaseLoginProvider(FirebaseAuth.getInstance())
  }

  private val authUI: AuthUI by lazy {
    AuthUI.getInstance()
  }

  private val localPhotoRepository: PhotoRepository by lazy {
    DevicePhotoRepository
  }

  private val remotePhotoRepository: PhotoRepository by lazy {
    FirebasePhotoRepository(FirebaseStorage.getInstance().reference)
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
    initRealm()
  }

  /**
   * Initialises app [Realm] instance with a specific configuration
   */
  private fun initRealm() {
    Realm.init(this)
  }

  companion object {
    lateinit var instance: App private set

    fun getPreferenceProvider(): PreferencesProvider =
      instance.prefsProvider

    fun getFormDataProvider(): FormDataProvider =
      instance.formDataProvider

    fun getLocalSurveysRepository(): SurveyRepository =
      instance.localSurveysRepository

    fun getRemoteSurveysRepository(): SurveyRepository =
      instance.remoteSurveysRepository

    fun getLoginProvider(): LoginProvider =
      instance.authProvider

    fun getLoginUI(): AuthUI =
      instance.authUI

    fun getDeviceFileDir(): File =
      instance.deviceFileDir

    fun getLocalPhotoRepository(): PhotoRepository =
      instance.localPhotoRepository

    fun getRemotePhotoRepository(): PhotoRepository =
      instance.remotePhotoRepository

    fun getTempFileProvider(): TemporaryFileProvider =
      instance.tempFileProvider
  }

}
