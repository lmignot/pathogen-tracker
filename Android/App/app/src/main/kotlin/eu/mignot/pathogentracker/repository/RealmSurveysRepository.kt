package eu.mignot.pathogentracker.repository

import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.realm.RealmObject

object RealmSurveysRepository: SurveyRepository {

  /**
   * @see SurveyRepository.getSurveys
   */
  override fun <T: RealmObject> getSurveys(surveyType: T): List<T> =
    surveyType.queryAll()

  /**
   * @see SurveyRepository.getSurveysFor
   */
  override fun <T: RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T> =
    surveyType.query { equalTo("batchId", parentId) }

  /**
   * @see SurveyRepository.getSurvey
   */
  override fun <T: RealmObject> getSurvey(surveyType: T, surveyId: String): T? =
    surveyType.queryFirst { equalTo("id", surveyId) }

  /**
   * @see SurveyRepository.storeSurvey
   */
  override fun <T: RealmObject> storeSurvey(survey: T) = survey.save()

  /**
   * @see SurveyRepository.getSurveysToSync
   */
  override fun <T: RealmObject> getSurveysToSync(surveyType: T): List<T> =
    surveyType.query { isNull("uploadedAt") }

}
