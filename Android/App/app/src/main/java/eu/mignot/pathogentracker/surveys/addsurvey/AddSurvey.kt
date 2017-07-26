package eu.mignot.pathogentracker.surveys.addsurvey

import android.view.View

interface AddSurvey {
  fun bind()

  fun unbind()

  fun setupToolbar()

  fun saveAndClose()

  fun cancelAndClose()
}
