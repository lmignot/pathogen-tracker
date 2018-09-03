package eu.mignot.pathogentracker.repository

import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.realm.RealmObject

object RealmSurveysRepository: SurveyRepository {

  override fun <T: RealmObject> getSurveys(surveyType: T): List<T> =
    surveyType.queryAll()

  override fun <T: RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T> =
    surveyType.query { equalTo("batchId", parentId) }

  override fun <T: RealmObject> getSurvey(surveyType: T, surveyId: String): T? =
    surveyType.queryFirst { equalTo("id", surveyId) }

  override fun <T: RealmObject> storeSurvey(survey: T) = survey.save()

  override fun <T: RealmObject> getSurveysToSync(surveyType: T): List<T> =
    surveyType.query { isNull("uploadedAt") }

}
