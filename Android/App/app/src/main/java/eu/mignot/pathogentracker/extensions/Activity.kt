package eu.mignot.pathogentracker.extensions

import android.support.design.widget.Snackbar
import android.view.View

fun showShortMessage(v: View, msg: String) {
  Snackbar
    .make(v, msg, Snackbar.LENGTH_SHORT)
    .show()
}

fun showLongMessage(v: View, msg: String) {
  Snackbar
    .make(v, msg, Snackbar.LENGTH_LONG)
    .show()
}
