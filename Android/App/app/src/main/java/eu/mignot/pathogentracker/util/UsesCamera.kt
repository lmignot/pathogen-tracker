package eu.mignot.pathogentracker.util

import android.Manifest
import android.app.Activity
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import pub.devrel.easypermissions.AfterPermissionGranted

interface UsesCamera {
  companion object {
    const val REQUEST_CODE = 2
    val PERMISSION = (Manifest.permission.CAMERA)
  }

  fun askForCameraPermission(activity: Activity, rationale: String) {
    EffortlessPermissions.requestPermissions(
      activity,
      rationale,
      REQUEST_CODE,
      PERMISSION
    )
  }

  @AfterPermissionGranted(REQUEST_CODE)
  fun requestCameraPermission() {}

  @AfterPermissionDenied(REQUEST_CODE)
  fun onCameraPermissionDenied() {}
}