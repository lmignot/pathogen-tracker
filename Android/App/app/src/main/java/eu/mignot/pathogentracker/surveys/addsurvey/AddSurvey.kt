package eu.mignot.pathogentracker.surveys.addsurvey

import android.support.v7.widget.Toolbar

interface AddSurvey {
  fun bind()

  fun unbind()

  fun setupToolbar(toolbar: Toolbar, title: String)

  fun saveAndClose()

  fun cancelAndClose()
}
