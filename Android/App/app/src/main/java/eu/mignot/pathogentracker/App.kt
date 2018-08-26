package eu.mignot.pathogentracker

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import eu.mignot.pathogentracker.data.AppFormDataProvider
import eu.mignot.pathogentracker.data.AppPreferencesProvider
import eu.mignot.pathogentracker.data.FormDataProvider
import eu.mignot.pathogentracker.data.PreferencesProvider
import eu.mignot.pathogentracker.surveys.data.HumanSurveyRepository
import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import eu.mignot.pathogentracker.surveys.data.VectorBatchSurveyRepository
import eu.mignot.pathogentracker.surveys.data.VectorSurveyRepository
import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import eu.mignot.pathogentracker.util.FirebaseLoginProvider
import eu.mignot.pathogentracker.util.NetworkUtils
import io.realm.Realm

class App : Application() {

  /*private val locationProvider by lazy {
    LocationServices.getFusedLocationProviderClient(this)
  }*/

  private val prefsProvider by lazy {
    AppPreferencesProvider
  }

  private val formDataProvider by lazy {
    AppFormDataProvider
  }

  private val vectorBatchSurveyRepository by lazy {
    VectorBatchSurveyRepository
  }

  private val vectorSurveyRepository by lazy {
    VectorSurveyRepository
  }

  private val humanSurveyRepository by lazy {
    HumanSurveyRepository
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

  override fun onCreate() {
    super.onCreate()
    instance = this
    Realm.init(this)
  }

  companion object {
    lateinit var instance: App private set
    fun getPreferenceProvider(): PreferencesProvider =
      instance.prefsProvider
    fun getFormDataProvider(): FormDataProvider =
      instance.formDataProvider
    fun getVectorBatchRepository(): SurveyRepository<VectorBatch> =
      instance.vectorBatchSurveyRepository
    fun getVectorRepository(): SurveyRepository<Vector> =
      instance.vectorSurveyRepository
    fun getHumanSurveyRepository(): SurveyRepository<Human> =
      instance.humanSurveyRepository
    fun getNetworkUtils() = NetworkUtils(instance.connectivityManager, instance.wifiManager)
    fun getLoginProvider(): FirebaseLoginProvider = instance.authProvider
    fun getLoginUI(): AuthUI = instance.authUI
  }

}
