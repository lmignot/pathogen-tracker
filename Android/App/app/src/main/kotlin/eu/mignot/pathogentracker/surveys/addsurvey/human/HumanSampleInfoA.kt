package eu.mignot.pathogentracker.surveys.addsurvey.human

import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Country
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.Infection
import eu.mignot.pathogentracker.data.models.database.SampleType
import kotlinx.android.synthetic.main.fragment_sample_info_1.*

class HumanSampleInfoA : StepFragment() {

  override val layoutResourceId: Int = R.layout.fragment_sample_info_1

  /**
   * @see StepFragment.getModel
   */
  override fun getModel(model: Human): Human {
    model.samples.addAll(surveySampleTypes.getAllValues().map{
      SampleType(it)
    })
    model.travelHistory.addAll(surveyTravelHistory.getAllValues().map{
      Country(it)
    })
    model.pastInfections.addAll(surveyInfectionHistory.getAllValues().map{
      Infection(it)
    })
    return model
  }

}
