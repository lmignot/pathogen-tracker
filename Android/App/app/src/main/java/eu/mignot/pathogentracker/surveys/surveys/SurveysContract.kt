package eu.mignot.pathogentracker.surveys.surveys

import eu.mignot.pathogentracker.common.mvp.MvpView
import eu.mignot.pathogentracker.common.mvp.Presenter
import eu.mignot.pathogentracker.surveys.data.models.survey.Survey

/**
 * Define a contract between the surveys view and its presenter
 */
interface SurveysContract {

  /**
   * Defines UI methods for [SurveysActivity]
   */
  interface View: MvpView {

    fun showSurveys(ls: List<Survey>): Unit

    fun showSurveyDetailView(s: Survey): Unit

    fun showAddSurvey(): Unit
  }

  /**
   * Defines user interaction(s) for [SurveysPresenter]
   */
  interface InteractionListener: Presenter<View> {

    fun loadSurveys(): Unit

    fun newSurvey(): Unit

    fun openSurveyDetail(s: Survey?): Unit
  }

}
