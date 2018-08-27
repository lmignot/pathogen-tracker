package eu.mignot.pathogentracker.util

import android.graphics.Bitmap

interface PhotoRepository {

  fun savePhoto(photoPath: String?, photo: Bitmap, isOptimized: Boolean): Boolean

}
