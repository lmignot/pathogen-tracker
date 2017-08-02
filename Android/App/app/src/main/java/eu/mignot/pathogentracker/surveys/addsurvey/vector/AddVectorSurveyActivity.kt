package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import eu.mignot.pathogentracker.MainActivity
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.extensions.*
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.util.SpinnerOrOther
import eu.mignot.pathogentracker.util.UsesCamera
import eu.mignot.pathogentracker.util.UsesGallery
import eu.mignot.pathogentracker.surveys.data.models.survey.Vector
import eu.mignot.pathogentracker.util.AppSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.indeterminateProgressDialog
import kotlinx.android.synthetic.main.activity_add_vector_survey.*
import kotlinx.android.synthetic.main.photo_bottom_sheet.*
import kotlinx.android.synthetic.main.vector_form.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import pub.devrel.easypermissions.AfterPermissionGranted

class AddVectorSurveyActivity: BaseSurveyActivity(), SpinnerOrOther, UsesCamera, UsesGallery {

  private val vectorId by lazy {
    intent.getStringExtra(AppSettings.Constants.VECTOR_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val vm by lazy {
    AddVectorViewModel(vectorId)
  }

  private val photoSheetBehavior by lazy {
    BottomSheetBehavior.from(photoSheet)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (vectorId == AppSettings.Constants.NO_ID_VALUE || batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<MainActivity>(AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_vector_no_batchid))
    }
    setContentView(R.layout.activity_add_vector_survey)
    setupToolbar(toolbarAV, getString(R.string.title_vector))
  }

  override fun bind() {
    setupSpinner(this, R.array.mosquitoSpecies, vectorSpecies, vectorSpeciesOtherLayout)
    setupPhotoSheetButton()
    requestCameraPermission()
    setDnaRequestListener()
  }

  override fun saveAndClose() {
    val progress = indeterminateProgressDialog(getString(R.string.notice_saving))
    val model = Vector(
      vectorId,
      batchId,
      getSpinnerValue(vectorSpecies, vectorSpeciesOther, vectorSpeciesOtherLayout),
      vectorGender.selectedValue(),
      vectorStage.selectedValue(),
      vectorDidFeed.asBoolean(),
      vectorDna.asString(),
      (photoView.drawable as BitmapDrawable).bitmap
    )
    vm
      .save(model)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy (
        onError = {
          progress.hide()
          showShortMessage(vectorSurveyRoot, "There was an error saving")
        },
        onSuccess = {
          progress.hide()
          showShortMessage(vectorSurveyRoot, "Successfully saved")
        }
      )
  }

  fun setupPhotoSheetButton() {
    photoSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    photoButton.setOnClickListener {
      if (photoSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
        photoSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      } else {
        photoSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  @AfterPermissionGranted(UsesCamera.REQUEST_CODE)
  override fun requestCameraPermission() {
    if (EffortlessPermissions.hasPermissions(this, UsesCamera.PERMISSION)) {
      setupPhotoListener()
    } else if (EffortlessPermissions.permissionPermanentlyDenied(this, UsesCamera.PERMISSION)) {
      fromCamera.visibility = View.GONE
      TODO("Set preference to not use camera")
    } else {
      askForCameraPermission(this, "Please allow access to the camera")
    }
  }

  @AfterPermissionDenied(UsesCamera.REQUEST_CODE)
  override fun onCameraPermissionDenied() {
    info("Camera permission denied")
    fromCamera.visibility = View.GONE
  }

  private fun setupPhotoListener() {
    fromCamera.setOnClickListener {
      val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      cameraIntent.resolveActivity(packageManager)?.let {
        photoSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        startActivityForResult(cameraIntent, UsesCamera.REQUEST_CODE)
      }
    }
    fromGallery.setOnClickListener {
      photoSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
      galleryIntent.type = "image/*"
      startActivityForResult(galleryIntent, UsesGallery.REQUEST_CODE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        UsesCamera.REQUEST_CODE -> {
          data?.let {
            val bmp = it.extras.get("data") as Bitmap
            setPhotoToView(bmp)
          }
        }
        UsesGallery.REQUEST_CODE -> {
          data?.data?.let {
            val bmp = MediaStore.Images.Media.getBitmap(contentResolver, it) as Bitmap
            setPhotoToView(bmp)
          }
        }
        else -> {}
      }
    } else {
      showShortMessage(vectorSurveyRoot, "There was an error choosing or taking the photo")
    }
  }

  private fun setPhotoToView(bmp: Bitmap) {
    photoView.setImageBitmap(bmp)
    photoView.visibility = View.VISIBLE
    photoButton.setText(R.string.replace_photo)
  }

  private fun setDnaRequestListener() {
    vectorDna.setOnClickListener {
      // @TODO: implement this
      showLongMessage(vectorSurveyRoot, "This hasn't yet been implemented :)")
    }
  }

}
