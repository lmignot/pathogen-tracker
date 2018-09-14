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

class AskForPermission : Fragment() {

  private val permissionCode by lazy {
    arguments!!.getInt(AppSettings.Constants.PERMISSION_CODE)
  }

  private val permissionRationaleId by lazy {
    arguments!!.getInt(AppSettings.Constants.PERMISSION_RATIONALE_ID)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_get_permission, container, false)
  }

  override fun onStart() {
    super.onStart()

    questionText.text = getText(permissionRationaleId)
    givePermission.onClick {
      (activity as OnBoarding).getPermission(permissionCode)
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
