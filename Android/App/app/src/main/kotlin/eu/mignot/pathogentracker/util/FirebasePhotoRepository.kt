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
import java.io.IOException
import java.util.*

class FirebasePhotoRepository(private val storageRef: StorageReference): PhotoRepository, AnkoLogger {

  override fun storePhoto(model: Photo, shouldOptimize: Boolean, photo: Bitmap?) =
    storePhotoToFirebase(model)

  private fun storePhotoToFirebase(photo: Photo) {
    val file = File(photo.path)
    val uploadRef = storageRef.child("$FIREBASE_PHOTO_PATH/${photo.fileName}")

    val stream = FileInputStream(file)

    try {
      info { "Started uploading photo ${photo.fileName} at ${System.currentTimeMillis()}" }
      uploadRef.putStream(stream)
        .addOnFailureListener {
          stream.close()
          error( "Photo ${photo.fileName} upload failed with error:\n ${it.localizedMessage}")
        }
        .addOnSuccessListener {
          stream.close()
          photo.uploadedAt = Date()
          photo.save()
          info { "Photo ${photo.fileName} uploaded successfully at ${System.currentTimeMillis()}" }
        }
    } catch (e: IOException) {
      error( "File Stream Error: ${e.localizedMessage}" )
    }
  }

}
