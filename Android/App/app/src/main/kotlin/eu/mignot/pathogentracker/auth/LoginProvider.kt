package eu.mignot.pathogentracker.auth

import com.google.firebase.auth.FirebaseUser

interface LoginProvider {

  /**
   * Return if a user is signed in
   */
  fun hasUser(): Boolean

  /**
   * Retrieve the currently signed-in user
   */
  fun getCurrentUser(): FirebaseUser?

  /**
   * Remove the signed-in user
   */
  fun signOut()

}
