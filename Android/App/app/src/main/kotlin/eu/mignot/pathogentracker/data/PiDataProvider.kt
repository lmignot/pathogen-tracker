package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.App

object PiDataProvider {

  private val networkUtils by lazy {
    App.getNetworkUtils()
  }

  fun getPiData(): Array<Byte> {
    TODO("N/A")
  }

}
