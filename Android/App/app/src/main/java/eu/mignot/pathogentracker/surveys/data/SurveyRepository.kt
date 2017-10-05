package eu.mignot.pathogentracker.surveys.data

import com.vicpin.krealmextensions.save
import io.realm.RealmObject

interface SurveyRepository<T: RealmObject> {

  fun getSurvey(surveyId: String): T?

  fun getSurveys(): List<T>

  fun saveSurvey(survey: T) = survey.save()

}
