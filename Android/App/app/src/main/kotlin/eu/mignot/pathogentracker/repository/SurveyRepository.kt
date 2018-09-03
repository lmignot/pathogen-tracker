package eu.mignot.pathogentracker.repository

import io.realm.RealmObject

interface SurveyRepository {

  fun <T: RealmObject> storeSurvey(survey: T)

  fun <T: RealmObject> getSurvey(surveyType: T, surveyId: String): T?

  fun <T: RealmObject> getSurveys(surveyType: T): List<T>

  fun <T: RealmObject> getSurveysToSync(surveyType: T): List<T>

  fun <T: RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T>

}
