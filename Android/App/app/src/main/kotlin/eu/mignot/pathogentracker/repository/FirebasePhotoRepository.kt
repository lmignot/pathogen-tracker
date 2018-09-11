package eu.mignot.pathogentracker.repository

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.AppSettings.Constants.FIREBASE_PHOTO_PATH
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class FirebasePhotoRepository(private val storageRef: StorageReference): PhotoRepository, AnkoLogger {

  override fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?) =
    storePhotoToFirebase(model)

  private fun storePhotoToFirebase(photo: Photo) {
    try {
      val file = File(photo.path)
      val uploadRef = storageRef.child("$FIREBASE_PHOTO_PATH/${photo.fileName}")
      val stream = FileInputStream(file)

      info { "Started uploading photo ${photo.fileName} at ${System.currentTimeMillis()}" }

      uploadRef.putStream(stream)
        .addOnFailureListener {
          // exception is logged, photo upload will be retried since uploadedAt
          // will not be set
          error("Photo ${photo.fileName} upload failed with error:\n ${it.localizedMessage}")
        }
        .addOnSuccessListener {
          photo.uploadedAt = Date()
          photo.save()
          info { "Photo ${photo.fileName} uploaded successfully at ${System.currentTimeMillis()}" }
        }
    } catch (ex: FileNotFoundException) {
      // If the file is not found, throw the exception.
      // This should not happen if the app is running correctly
      throw(ex)
    } catch (ex: IOException) {
      error { "IOException occurred when uploading photo with message: ${ex.localizedMessage}" }
    }
  }

  override fun retrievePhoto(path: String): Bitmap? =
    TODO(AppSettings.Constants.REPOSITORY_NI_RATIONALE)

}
