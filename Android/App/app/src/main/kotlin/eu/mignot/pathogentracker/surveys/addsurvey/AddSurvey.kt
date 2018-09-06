package eu.mignot.pathogentracker.surveys.addsurvey

interface AddSurvey<out T> {

  /**
   * Bind any listeners, initialize
   * the ViewModel
   */
  fun bind() {}

  /**
   * Unbind any listeners
   */
  fun unbind()

  /**
   * Save the survey and return to main activity
   */
  fun saveAndClose()

  /**
   * Cancel survey and return to main activity
   */
  fun cancelAndClose()

  /**
   * Prepare the survey model with user input
   */
  fun getModel(): T

}
