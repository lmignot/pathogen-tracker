package eu.mignot.pathogentracker.repository

import com.vicpin.krealmextensions.*
import eu.mignot.pathogentracker.data.models.database.*
import io.realm.RealmConfiguration
import io.realm.RealmObject

class RealmSurveysRepository(config: RealmConfiguration): SurveyRepository {

  init {
    // initialize all Realm modules with configuration
    RealmConfigStore.initModule(Human::class.java, config)
    RealmConfigStore.initModule(Country::class.java, config)
    RealmConfigStore.initModule(Infection::class.java, config)
    RealmConfigStore.initModule(Location::class.java, config)
    RealmConfigStore.initModule(Photo::class.java, config)
    RealmConfigStore.initModule(SampleType::class.java, config)
    RealmConfigStore.initModule(Symptom::class.java, config)
    RealmConfigStore.initModule(Vector::class.java, config)
    RealmConfigStore.initModule(VectorBatch::class.java, config)
  }

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
