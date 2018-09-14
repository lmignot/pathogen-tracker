package eu.mignot.pathogentracker.auth

import com.google.firebase.auth.FirebaseAuth

class FirebaseLoginProvider(private val authProvider: FirebaseAuth): LoginProvider {

  /**
   * @see LoginProvider.hasUser
   */
  override fun hasUser(): Boolean =
    authProvider.currentUser != null

  /**
   * @see LoginProvider.getCurrentUser
   */
  override fun getCurrentUser() = authProvider.currentUser

  /**
   * @see LoginProvider.signOut
   */
  override fun signOut() = authProvider.signOut()

}
