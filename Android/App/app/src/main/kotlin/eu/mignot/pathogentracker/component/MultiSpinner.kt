package eu.mignot.pathogentracker.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import eu.mignot.pathogentracker.R
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * A MultiSpinner.
 *
 * Custom android view component that allows multiple values to be added and removed
 * from a discrete set. It uses a [Spinner] to display the available values
 * @see [LinearLayout] for constructor params
 */
class MultiSpinner(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
  LinearLayout(context, attrs, defStyleAttr) {

  private lateinit var button: View

  private lateinit var spinnerContainer: LinearLayout

  private lateinit var adapter: ArrayAdapter<CharSequence>

  private var spinnerWidgets: List<Spinner> = listOf()

  /**
   * Configurable text displayed as the field's label
   */
  private lateinit var labelText: String

  /**
   * Configurable text displayed on the "add more" button
   */
  private lateinit var buttonText: String

  /**
   * Configurable maximum number of items a user can select
   */
  private var maxItems: Int = 0

  /**
   * Configurable array resource containing the values
   */
  var valuesResId: Int = 0

  // Retrieve configuration attributes from xml layout declaration
  init {
    val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MultiSpinner, 0, 0)
    a?.let {
      labelText = it.getString(R.styleable.MultiSpinner_labelText) ?: "Choose one"
      buttonText = it.getString(R.styleable.MultiSpinner_buttonText) ?: "Add another"
      valuesResId = it.getResourceId(R.styleable.MultiSpinner_valuesResId, 0)
      maxItems = it.getInt(R.styleable.MultiSpinner_maxItems, 1)
      it.recycle()
    }
    init()
  }

  // Use Anko DSL to construct the component's layout
  private fun init() = AnkoContext.createDelegate(this).apply {
    adapter = ArrayAdapter.createFromResource(context, valuesResId, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    linearLayout {
      orientation = LinearLayout.VERTICAL
      lparams(matchParent, wrapContent)
      textView(labelText) {
        textSize = 13f
      }.lparams(matchParent, wrapContent) {
        marginStart = dip(4)
      }
      spinnerContainer = linearLayout {
        topPadding = dip(4)
        orientation = LinearLayout.VERTICAL
        dividerDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.colorDivider))
        dividerPadding = dip(4)
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        lparams(matchParent, wrapContent) {
          topMargin = dip(8)
        }
        val firstSpinner = spinner()
        firstSpinner.lparams(matchParent, wrapContent) {
          marginStart = dip(24)
          bottomMargin = dip(8)
        }
        spinnerWidgets += firstSpinner

        firstSpinner.adapter = adapter
      }
      button = button(buttonText) {
        backgroundColor = Color.TRANSPARENT
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        textColor = ContextCompat.getColor(context, R.color.colorSecondaryText)
        onClick {
          if (spinnerWidgets.size < maxItems) {
            button.isEnabled = true
            addSpinner()
          } else {
            button.isEnabled = false
          }
        }
      }.lparams(matchParent, wrapContent) {
        marginStart = dip(32)
      }
    }
  }

  /**
   * Retrieve all the selected values
   */
  fun getAllValues(): List<String> {
    return spinnerWidgets.map { it.selectedItem.toString() }.filter { it != "Select…" }
  }

  /**
   * Adds a spinner when the button is clicked
   */
  private fun addSpinner() {
    val layout = linearLayout {
      orientation = LinearLayout.HORIZONTAL
      lparams(matchParent, wrapContent)
    }
    val spinner = spinner()
    spinner.layoutParams = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT)
    val btn = imageButton(R.drawable.close_black) {
      backgroundColor = Color.TRANSPARENT
      onClick {
        spinnerWidgets -= spinner
        spinnerContainer.removeView(layout)
        button.isEnabled = true
      }
    }
    btn.layoutParams = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.MATCH_PARENT)

    (btn.parent as ViewGroup).removeView(btn)
    (spinner.parent as ViewGroup).removeView(spinner)
    AnkoInternals.addView(layout, btn)
    AnkoInternals.addView(layout, spinner)
    spinnerWidgets += spinner
    (layout.parent as ViewGroup).removeView(layout)
    AnkoInternals.addView(spinnerContainer, layout)

    spinner.adapter = adapter

    invalidate()
    requestLayout()
  }

  constructor(context: Context): this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.multiSpinner(theme: Int = 0) = multiSpinner({}, theme)
inline fun ViewManager.multiSpinner(init: MultiSpinner.() -> Unit, theme: Int = 0)
  = ankoView(::MultiSpinner, theme, init)
