package eu.mignot.pathogentracker.util

import android.Manifest
import android.app.Activity
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import pub.devrel.easypermissions.AfterPermissionGranted

interface UsesCamera {
  companion object {
    const val REQUEST_CODE = AppSettings.Constants.CAMERA_REQUEST_CODE
    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
  }

  fun askForCameraPermission(activity: Activity, rationale: String) {
    EffortlessPermissions.requestPermissions(
      activity,
      rationale,
      REQUEST_CODE,
      CAMERA_PERMISSION,
      STORAGE_PERMISSION
    )
  }

  @AfterPermissionGranted(REQUEST_CODE)
  fun onRequestCameraPermission() {}

  @AfterPermissionDenied(REQUEST_CODE)
  fun onCameraPermissionDenied() {}
}
