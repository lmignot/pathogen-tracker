package eu.mignot.pathogentracker.surveys.data

import eu.mignot.pathogentracker.surveys.data.models.database.Human
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.data.models.database.VectorBatch
import io.realm.RealmObject

interface SurveyRepository {

  fun <T: RealmObject> storeSurvey(survey: T)

  fun <T: RealmObject, L: List<T>> storeSurveys(surveys: L)

  fun <T: RealmObject> getSurvey(surveyType: T, surveyId: String): T?

  fun <T: RealmObject> getSurveys(surveyType: T): List<T>

  fun <T: RealmObject> getSurveysToSync(surveyType: T): List<T>

  fun <T: RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T>

}
