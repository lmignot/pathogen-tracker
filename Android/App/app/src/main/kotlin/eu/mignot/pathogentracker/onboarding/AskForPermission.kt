package eu.mignot.pathogentracker.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.util.AppSettings
import kotlinx.android.synthetic.main.fragment_get_permission.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * A Fragment that supports requesting any type
 * of device permission provided the arguments are supplied
 */
class AskForPermission : Fragment() {

  /**
   * The permission code for the permission request Intent
   */
  private val permissionCode by lazy {
    arguments!!.getInt(AppSettings.Constants.PERMISSION_CODE)
  }

  /**
   * The permission rationale id for the permission request Intent
   */
  private val permissionRationaleId by lazy {
    arguments!!.getInt(AppSettings.Constants.PERMISSION_RATIONALE_ID)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_get_permission, container, false)
  }

  override fun onStart() {
    super.onStart()

    questionText.text = getText(permissionRationaleId)

    // let the activity handle the permission request
    // so it can present the next view when permission
    // has been granted
    givePermission.onClick {
      (activity as OnBoarding).requestPermission(permissionCode)
    }
  }

  companion object {
      fun newInstance(permissionCode: Int, permissionRationaleId: Int): AskForPermission {
        val fragment = AskForPermission()
        val args = Bundle()
        args.putInt(AppSettings.Constants.PERMISSION_CODE, permissionCode)
        args.putInt(AppSettings.Constants.PERMISSION_RATIONALE_ID, permissionRationaleId)
        fragment.arguments = args
        return fragment
      }
  }
}
