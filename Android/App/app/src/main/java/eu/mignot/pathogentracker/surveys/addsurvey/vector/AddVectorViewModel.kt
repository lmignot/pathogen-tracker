package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.graphics.Bitmap
import eu.mignot.pathogentracker.surveys.data.models.survey.Vector
import io.reactivex.Single
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AddVectorViewModel(val batchId: String, val id: String): AnkoLogger {

  private fun setCurrentImage(b: Bitmap?) {
    b?.let{ image = it }
  }

  var image: Bitmap?
    get() = image?.let{ it }
    set(b) = setCurrentImage(b)

  fun save(v: Vector): Single<Boolean> {
    info(v.toString())
    return Single.just(true)
  }

}
