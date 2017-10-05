package eu.mignot.pathogentracker.surveys.addsurvey

interface AddSurvey<out T> {

  fun bind() {}

  fun unbind()

  fun saveAndClose()

  fun cancelAndClose()

  fun getModel(): T

}
