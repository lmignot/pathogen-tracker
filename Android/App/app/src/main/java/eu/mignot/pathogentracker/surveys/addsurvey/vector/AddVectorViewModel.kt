package eu.mignot.pathogentracker.surveys.addsurvey.vector

import eu.mignot.pathogentracker.surveys.data.models.survey.Vector
import io.reactivex.Single
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AddVectorViewModel(val batchId: String, val id: String): AnkoLogger {

  fun save(v: Vector): Single<Boolean> {
    info(v.toString())
    return Single.just(true)
  }

}
