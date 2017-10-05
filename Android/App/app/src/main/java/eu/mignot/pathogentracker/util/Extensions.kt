package eu.mignot.pathogentracker.util

import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.*

fun Date.formatTime(format: String = AppSettings.Constants.DATE_TIME_FORMAT): String =
  DateFormat.format(format, this).toString()

fun Date.formatDate(format: String = AppSettings.Constants.DATE_FORMAT): String =
  DateFormat.format(format, this).toString()

/**
 * Convenience extension to get an [EditText]
 * field's value as an Int, with a default value
 * as fallback if the field is empty
 *
 * @param default The default value
 * @return The value as an Int or the default
 */
fun EditText.asIntOrDefault(default: Int): Int {
  return try {
    this.text.toString().toInt()
  } catch (ne: NumberFormatException) {
    default
  }
}

/**
 * Convenience extension to get the string value
 * of the selected [RadioButton] in a [RadioGroup]
 *
 * @return The selected button's value or an empty String
 */
fun RadioGroup.selectedValue(): String {
  val id = this.checkedRadioButtonId
  return if (id > -1) {
    val selected = this.findViewById(id) as RadioButton
    selected.text.toString()
  } else ""
}

/**
 * Convenience extension to get a "Yes/No" [RadioGroup]
 * value as a Boolean.
 *
 * @return True if the button with "Yes" is selected, else false
 */
fun RadioGroup.asBoolean(): Boolean = this.selectedValue() == "Yes"

/**
 * Get a Boolean value as a "yes/no" string
 *
 * @return Yes if true, No if false
 */
fun Boolean.asYesOrNo(): String = if (this) "Yes" else "No"


/**
 * Convenience extension to show a short
 * [Snackbar] message
 *
 * @param v The [View] the [Snackbar] will be attached to
 * @param msg The message to display
 */
fun showShortMessage(v: View, msg: String) {
  Snackbar
    .make(v, msg, Snackbar.LENGTH_SHORT)
    .show()
}

/**
 * Convenience extension to show a long
 * [Snackbar] message
 *
 * @param v The [View] the [Snackbar] will be attached to
 * @param msg The message to display
 */
fun showLongMessage(v: View, msg: String) {
  Snackbar
    .make(v, msg, Snackbar.LENGTH_LONG)
    .show()
}

/**
 * Convenience extension function for setting up a
 * toolbar with a specific title and drawable
 *
 * @param toolbar Toolbar to set up
 * @param title Title to set
 * @param homeIndicator Drawable id to use for the main icon
 */
fun AppCompatActivity.setupToolbar(toolbar: Toolbar, title: String, @DrawableRes homeIndicator: Int) {
  setSupportActionBar(toolbar)
  supportActionBar?.title = title
  supportActionBar?.setDisplayHomeAsUpEnabled(true)
  supportActionBar?.setHomeAsUpIndicator(homeIndicator)
}
