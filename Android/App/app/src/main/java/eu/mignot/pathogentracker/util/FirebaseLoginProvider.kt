package eu.mignot.pathogentracker.util

import com.google.firebase.auth.FirebaseAuth

class FirebaseLoginProvider(private val authProvider: FirebaseAuth) {

  fun hasUser(): Boolean =
    authProvider.currentUser != null

  fun getCurrentUser() = authProvider.currentUser

}
