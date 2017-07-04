package eu.mignot.pathogentracker.common.mvp

interface Presenter<in V: MvpView> {

  fun attach(v: V): Unit

  fun detach(): Unit

  fun handleError(e: Error): Unit
}
