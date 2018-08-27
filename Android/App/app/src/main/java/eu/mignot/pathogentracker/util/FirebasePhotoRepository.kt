package eu.mignot.pathogentracker.util

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference

class FirebasePhotoRepository(storageReference: StorageReference): PhotoRepository {

  override fun savePhoto(photoPath: String?, photo: Bitmap, isOptimized: Boolean): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
