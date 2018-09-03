package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import eu.mignot.pathogentracker.data.models.database.Photo

interface PhotoRepository {

  fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?)

}
