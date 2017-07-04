package eu.mignot.pathogentracker.common.mvp

interface MvpView {

  fun toggleActivity(isActive: Boolean): Unit

  fun onError(msg: String): Unit
}
