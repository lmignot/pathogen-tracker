package eu.mignot.pathogentracker.surveys.addsurvey

interface AddSurvey {
  fun bind()

  fun unbind()

  fun setupToolbar()

  fun saveAndClose()

  fun cancelAndClose()
}
