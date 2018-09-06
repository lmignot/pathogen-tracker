package eu.mignot.pathogentracker.repository

import io.realm.RealmObject

interface SurveyRepository {

  /**
   * Store a survey in the repository
   *
   * @param survey The survey to store
   */
  fun <T: RealmObject> storeSurvey(survey: T)

  /**
   * Fetch a survey of a given type from the repository
   *
   * @param surveyType the type of survey to retrieve
   * @param surveyId the survey's id
   */
  fun <T: RealmObject> getSurvey(surveyType: T, surveyId: String): T?

  /**
   * Fetch a list of surveys of a given type from the repository
   *
   * @param surveyType the type of surveys to retrieve
   */
  fun <T: RealmObject> getSurveys(surveyType: T): List<T>

  /**
   * Fetch a list of surveys that need to be synced between repositories
   *
   * @param surveyType the type of surveys to retrieve
   */
  fun <T: RealmObject> getSurveysToSync(surveyType: T): List<T>

  /**
   * Fetch a list of surveys with a given parent record
   *
   * @param surveyType the type of surveys to retrieve
   * @param parentId the parent record id
   */
  fun <T: RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T>

}
