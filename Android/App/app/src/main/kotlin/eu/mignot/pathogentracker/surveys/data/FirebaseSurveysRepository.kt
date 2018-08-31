package eu.mignot.pathogentracker.surveys.data

import eu.mignot.pathogentracker.util.AppSettings
import io.realm.RealmObject

object FirebaseSurveysRepository: SurveyRepository {

  private const val NOT_IMPLEMENTED_RATIONALE = AppSettings.Constants.REPOSITORY_NI_RATIONALE

  override fun <T : RealmObject> getSurvey(surveyType: T, surveyId: String): T? =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  override fun <T : RealmObject> getSurveys(surveyType: T): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  override fun <T : RealmObject> getSurveysToSync(surveyType: T): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  override fun <T : RealmObject> getSurveysFor(surveyType: T, parentId: String): List<T> =
    TODO(NOT_IMPLEMENTED_RATIONALE)

  override fun <T: RealmObject, L: List<T>> storeSurveys(surveys: L) = TODO("n/a")

  override fun <T: RealmObject> storeSurvey(survey: T) = TODO("n/a")

}
