package eu.mignot.pathogentracker.surveys.addsurvey.vector

import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.data.models.ui.UiLocation
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.BaseViewModel
import eu.mignot.pathogentracker.surveys.addsurvey.CurrentDateProvider
import org.jetbrains.anko.AnkoLogger
import java.util.*

class AddVectorBatchViewModel(
  surveyRepository: SurveyRepository
): AnkoLogger, BaseViewModel<VectorBatch>(surveyRepository), CurrentDateProvider {

  companion object {
      const val PREFIX = "VB"
  }

  override val id: String by lazy { "$PREFIX-${UUID.randomUUID()}" }

  override val currentDate: Calendar by lazy { Calendar.getInstance() }

  var location: UiLocation? = null

  var territory: String? = null

}
