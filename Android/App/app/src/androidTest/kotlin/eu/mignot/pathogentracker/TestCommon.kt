package eu.mignot.pathogentracker

import com.google.firebase.auth.FirebaseAuth
import com.vicpin.krealmextensions.deleteAll
import eu.mignot.pathogentracker.data.models.database.*
import eu.mignot.pathogentracker.data.models.database.Vector
import java.util.*

object TestCommon {

  const val TEST_LAT = 37.377166
  const val TEST_LONG = -122.086966
  const val TEST_ACC= 2.0F
  const val TEST_USER_EMAIl = "testuser@pathogentrackerapp.gmail.com"
  const val TEST_USER_PASSWORD = "as\$h26mPO2zF"

  private val loginProvider = App.getLoginProvider()

  fun signOut() {
    loginProvider.signOut()
  }

  fun signIn() {
    FirebaseAuth
      .getInstance()
      .signInWithEmailAndPassword(TEST_USER_EMAIl, TEST_USER_PASSWORD)
  }

  fun createLocation(): Location {
    return with(Location()) {
      this.latitude = TEST_LAT
      this.longitude = TEST_LONG
      this.accuracy = TEST_ACC
      this
    }
  }

  fun createHuman(id: String, date: Date, uploadedAt: Date?): Human {
    return with(Human()) {
      this.id = id
      collectedOn = date
      locationCollected = createLocation()
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
  }

  fun createVectorBatch(id: String, date: Date, uploadedAt: Date?): VectorBatch {
    return with(VectorBatch()) {
      this.id = id
      collectedOn = date
      locationCollected = createLocation()
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
  }

  fun createVector(id: String, batchId: String, uploadedAt: Date?): Vector {
    return with(Vector()) {
      this.id = id
      this.batchId = batchId
      uploadedAt?.let {
        this.uploadedAt = it
      }
      this
    }
  }

  fun clearRealm() {
    Location().deleteAll()
    Photo().deleteAll()
    Vector().deleteAll()
    VectorBatch().deleteAll()
    Human().deleteAll()
  }
}
