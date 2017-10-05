package eu.mignot.pathogentracker.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import eu.mignot.pathogentracker.App
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

object NetworkUtils: AnkoLogger {

  private lateinit var cm: ConnectivityManager
  private lateinit var wm: WifiManager

  operator fun invoke(cm: ConnectivityManager, wm: WifiManager): NetworkUtils {
    this.cm = cm
    this.wm = wm
    return this
  }

  fun scanForPi() = wm.startScan()

  fun isPiSSIDAvailable(): Boolean {
    info(wm.scanResults.map{ it.SSID }.toString())
    info("bbk" == AppSettings.Constants.PI_SSID)
    info("bbk" === AppSettings.Constants.PI_SSID)
    return wm.scanResults.any { it.SSID == AppSettings.Constants.PI_SSID }
  }

  fun connectToPi(): Boolean {
    if (isConnectedToPi()) return true

    val piNetworkId: Int = when (hasConnectedToPi()) {
      true -> App.getPreferenceProvider().getPiNetworkId()
      else -> {
        val conf = WifiConfiguration()
        conf.SSID = "\"${AppSettings.Constants.PI_SSID}\""
        conf.preSharedKey = "\"${AppSettings.Constants.PI_PASSWORD}\""
        wm.addNetwork(conf)
      }
    }

    if (!hasConnectedToPi()) App.getPreferenceProvider().setPiNetworkId(piNetworkId)

    wm.disconnect()
    wm.enableNetwork(piNetworkId, false)
    return wm.reconnect()
  }

  private fun isConnectedToWifi(): Boolean {
    return when(wm.isWifiEnabled) {
      true -> when(cm.activeNetworkInfo!!.isConnectedOrConnecting) {
        true -> cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI
        else -> false
      }
      else -> {
        wm.isWifiEnabled = true
        isConnectedToWifi()
      }
    }
  }

  fun isConnectedToPi(): Boolean {
    return when (isConnectedToWifi()) {
      true -> wm.connectionInfo.ssid === AppSettings.Constants.PI_SSID
      else -> false
    }
  }

  fun hasConnectedToPi(): Boolean =
    App.getPreferenceProvider().getPiNetworkId() != -1

}
