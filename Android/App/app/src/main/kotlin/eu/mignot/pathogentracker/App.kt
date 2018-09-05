package eu.mignot.pathogentracker

import android.app.Application
import android.os.Environment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.vicpin.krealmextensions.RealmConfigStore
import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.data.models.database.*
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.repository.*
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.FirebaseLoginProvider
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

  private val deviceFileDir by lazy {
    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  }

  private val prefsProvider by lazy {
    AppPreferencesProvider
  }

  private val formDataProvider by lazy {
    AppFormDataProvider
  }

  private val localSurveysRepository by lazy {
    RealmSurveysRepository
  }

  private val fireStoreInstance by lazy {
    val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(false)
      .setTimestampsInSnapshotsEnabled(true)
      .build()
    val db = FirebaseFirestore.getInstance()
    db.firestoreSettings = settings
    db
  }

  private val remoteSurveysRepository by lazy {
    FirebaseSurveysRepository(
      fireStoreInstance
    )
  }

  private val authProvider by lazy {
    FirebaseLoginProvider(FirebaseAuth.getInstance())
  }

  private val authUI by lazy {
    AuthUI.getInstance()
  }

  private val localPhotoRepository by lazy {
    DevicePhotoRepository
  }

  private val remotePhotoRepository by lazy {
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
    val config = RealmConfiguration
      .Builder()
      .name(AppSettings.Constants.REALM_DB_NAME)
      .schemaVersion(AppSettings.Constants.REALM_SCHEMA_VERSION)
      .deleteRealmIfMigrationNeeded()
      .build()
    RealmConfigStore.initModule(Human::class.java, config)
    RealmConfigStore.initModule(Country::class.java, config)
    RealmConfigStore.initModule(Infection::class.java, config)
    RealmConfigStore.initModule(Location::class.java, config)
    RealmConfigStore.initModule(Photo::class.java, config)
    RealmConfigStore.initModule(SampleType::class.java, config)
    RealmConfigStore.initModule(Symptom::class.java, config)
    RealmConfigStore.initModule(Vector::class.java, config)
    RealmConfigStore.initModule(VectorBatch::class.java, config)
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
    fun getLoginProvider(): FirebaseLoginProvider = instance.authProvider
    fun getLoginUI(): AuthUI = instance.authUI
    fun getDeviceFileDir() = instance.deviceFileDir!!
    fun getLocalPhotoRepository() = instance.localPhotoRepository
    fun getRemotePhotoRepository() = instance.remotePhotoRepository
  }

}