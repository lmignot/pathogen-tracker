package eu.mignot.pathogentracker.util

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import eu.mignot.pathogentracker.extensions.asString

interface SpinnerOrOther {

  fun setupSpinner(
    context: Context,
    arraySource: Int,
    spinnerField: Spinner,
    otherField: TextInputLayout) {
    val adapter = ArrayAdapter.createFromResource(context, arraySource, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinnerField.adapter = adapter
    spinnerField.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos).toString()
        when(selected) {
          "Other" -> {toggleOtherField(true, otherField)}
          else -> {toggleOtherField(field = otherField)}
        }
      }
      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
  }

  fun getSpinnerValue(spinner: Spinner, textField: EditText, textLayout: TextInputLayout): String {
    if (textLayout.visibility == View.VISIBLE) {
      return textField.asString()
    } else {
      val selected = spinner.getItemAtPosition(spinner.selectedItemPosition)
      return selected.toString()
    }
  }

  private fun toggleOtherField(visible: Boolean = false, field: TextInputLayout) {
    if (visible) {
      field.visibility = View.VISIBLE
    } else {
      field.visibility = View.GONE
    }
  }
}
