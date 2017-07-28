package eu.mignot.pathogentracker.extensions

import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

fun EditText.asIntOrDefault(default: Int): Int {
  val value = this.text.toString()
  try {
    return value.toInt()
  } catch (ne: NumberFormatException) {
    return default
  }
}

fun EditText.asString() = this.text.toString()

fun RadioGroup.selectedValue(): String {
  val id = this.checkedRadioButtonId
  if (id > -1) {
    val selected = findViewById(id) as RadioButton
    return selected.text.toString()
  }
  return ""
}
