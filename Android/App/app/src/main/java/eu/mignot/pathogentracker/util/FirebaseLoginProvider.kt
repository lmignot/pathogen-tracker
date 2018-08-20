package eu.mignot.pathogentracker.util

import com.google.firebase.auth.FirebaseAuth

class FirebaseLoginProvider(private val authProvider: FirebaseAuth) {

  /**
   * Return if a user is signed in
   */
  fun hasUser(): Boolean =
    authProvider.currentUser != null

  /**
   * @see FirebaseAuth.getCurrentUser
   */
  fun getCurrentUser() = authProvider.currentUser

  /**
   * @see FirebaseAuth.signOut
   */
  fun signOut() = authProvider.signOut()

}
