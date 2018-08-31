package eu.mignot.pathogentracker.util

import android.graphics.Bitmap
import eu.mignot.pathogentracker.surveys.data.models.database.Photo

interface PhotoRepository {

  fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?)

}
