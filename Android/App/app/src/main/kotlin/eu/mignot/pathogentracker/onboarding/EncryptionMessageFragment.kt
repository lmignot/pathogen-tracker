package eu.mignot.pathogentracker.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.mignot.pathogentracker.R
import kotlinx.android.synthetic.main.fragment_encryption_msg.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class EncryptionMessageFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_encryption_msg, container, false)
  }

  override fun onStart() {
    super.onStart()

    finishOnboarding.onClick {
      (activity as OnBoarding).isComplete()
    }
  }

  companion object {
    fun newInstance() = EncryptionMessageFragment()
  }

}
