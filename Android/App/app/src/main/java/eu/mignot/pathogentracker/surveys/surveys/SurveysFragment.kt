package eu.mignot.pathogentracker.surveys.surveys

import android.support.v4.app.Fragment
import eu.mignot.pathogentracker.surveys.data.models.survey.Survey

class SurveysFragment: Fragment(), SurveysContract.View {
  override fun toggleActivity(isActive: Boolean) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onError(msg: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSurveys(ls: List<Survey>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSurveyDetailView(s: Survey) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showAddSurvey() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
