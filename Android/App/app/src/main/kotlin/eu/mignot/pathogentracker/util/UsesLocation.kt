package eu.mignot.pathogentracker.util

import android.Manifest
import android.app.Activity
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions

interface UsesLocation {
  companion object {
    const val REQUEST_CODE = AppSettings.RequestCodes.LOCATION_REQ_CODE
    const val PERMISSION = (Manifest.permission.ACCESS_FINE_LOCATION)
  }

  /**
   * @see EffortlessPermissions.requestPermissions
   */
  fun askForLocationPermission(activity: Activity, rationale: String) {
    EffortlessPermissions.requestPermissions(
      activity,
      rationale,
      REQUEST_CODE,
      PERMISSION
    )
  }

}
