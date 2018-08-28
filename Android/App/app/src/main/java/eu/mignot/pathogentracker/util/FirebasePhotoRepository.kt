package eu.mignot.pathogentracker.util

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference
import com.vicpin.krealmextensions.save
import eu.mignot.pathogentracker.surveys.data.models.database.Photo
import eu.mignot.pathogentracker.util.AppSettings.Constants.FIREBASE_PHOTO_PATH
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File
import java.io.FileInputStream
import java.util.*

class FirebasePhotoRepository(private val storageRef: StorageReference): PhotoRepository, AnkoLogger {

  override fun storePhoto(model: Photo, isOptimized: Boolean, photo: Bitmap?) {
    storePhotoToFirebase(model)
  }

  private fun storePhotoToFirebase(photo: Photo) {
    val file = File(photo.path)
    val uploadRef = storageRef.child("$FIREBASE_PHOTO_PATH/${photo.fileName}")
    return FileInputStream(file).use {
      info { "Started uploading photo ${photo.fileName} at ${System.currentTimeMillis()}" }
      uploadRef.putStream(it)
        .addOnFailureListener {
          error( "Photo ${photo.fileName} upload failed with error:\n ${it.localizedMessage}")
        }
        .addOnSuccessListener {
          photo.uploadedAt = Date()
          photo.save()
          info { "Photo ${photo.fileName} uploaded successfully at ${System.currentTimeMillis()}" }
        }
    }
  }

}
