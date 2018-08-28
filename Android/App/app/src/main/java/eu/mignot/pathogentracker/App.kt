package eu.mignot.pathogentracker

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Environment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.surveys.data.*
import eu.mignot.pathogentracker.util.DevicePhotoRepository
import eu.mignot.pathogentracker.util.FirebaseLoginProvider
import eu.mignot.pathogentracker.util.FirebasePhotoRepository
import eu.mignot.pathogentracker.util.NetworkUtils
import io.realm.Realm

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

  private val realmSurveysRepository by lazy {
    RealmSurveysRepository
  }

  private val connectivityManager by lazy {
    instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  private val wifiManager by lazy {
    instance.getSystemService(Context.WIFI_SERVICE) as WifiManager
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
    Realm.init(this)
  }

  private fun startPhotoJob() {
    
  }

  companion object {
    lateinit var instance: App private set
    fun getPreferenceProvider(): PreferencesProvider =
      instance.prefsProvider
    fun getFormDataProvider(): FormDataProvider =
      instance.formDataProvider
    fun getSurveysRepository(): SurveyRepository =
      instance.realmSurveysRepository
    fun getNetworkUtils() = NetworkUtils(instance.connectivityManager, instance.wifiManager)
    fun getLoginProvider(): FirebaseLoginProvider = instance.authProvider
    fun getLoginUI(): AuthUI = instance.authUI
    fun getDeviceFileDir() = instance.deviceFileDir!!
    fun getLocalPhotoRepository() = instance.localPhotoRepository
    fun getRemotePhotoRepository() = instance.remotePhotoRepository
  }

}
