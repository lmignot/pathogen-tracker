package eu.mignot.pathogentracker.util

import android.Manifest
import android.app.Activity
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import pub.devrel.easypermissions.AfterPermissionGranted

interface UsesLocation {
  companion object {
    const val REQUEST_CODE = 1
    const val PERMISSION = (Manifest.permission.ACCESS_FINE_LOCATION)
  }

  fun askForLocationPermission(activity: Activity, rationale: String) {
    EffortlessPermissions.requestPermissions(
      activity,
      rationale,
      REQUEST_CODE,
      PERMISSION
    )
  }

}
