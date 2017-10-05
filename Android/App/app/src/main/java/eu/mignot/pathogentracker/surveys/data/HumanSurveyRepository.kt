package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import eu.mignot.pathogentracker.surveys.data.models.database.Human

object HumanSurveyRepository: SurveyRepository<Human> {

  override fun getSurvey(surveyId: String): Human? =
    Human().queryFirst { q -> q.equalTo("id", surveyId) }

  override fun getSurveys(): List<Human> = Human().queryAll()

}
