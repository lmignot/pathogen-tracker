package eu.mignot.pathogentracker.surveys.addsurvey

import eu.mignot.pathogentracker.surveys.data.SurveyRepository
import io.realm.RealmObject

abstract class BaseViewModel<in T: RealmObject>(private val repository: SurveyRepository) {

  abstract val id: String

  fun save(model: T) = repository.storeSurvey(model)
}
