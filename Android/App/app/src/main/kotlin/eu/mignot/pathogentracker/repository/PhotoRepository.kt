package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import eu.mignot.pathogentracker.data.models.database.Photo

interface PhotoRepository {

  /**
   * Store a photo in the repository
   *
   * @param model: [Photo] The photo model to use for storage
   * @param shouldOptimize Whether the image should be compressed or not
   * @param photo The bitmap to store
   */
  fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?)

  /**
   * Retrieve a photo from the repository
   *
   * @param path The path to the photo
   */
  fun retrievePhoto(path: String): Bitmap?

}
