package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.FileProvider
import android.view.View
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.surveydetail.VectorBatchDetailActivity
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.*
import kotlinx.android.synthetic.main.activity_add_vector_survey.*
import kotlinx.android.synthetic.main.photo_bottom_sheet.*
import kotlinx.android.synthetic.main.vector_form.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File

class AddVectorSurveyActivity: BaseSurveyActivity<Vector>(), SpinnerOrOther, UsesCamera, UsesGallery {

  private val vectorId by lazy {
    intent.getStringExtra(AppSettings.Constants.VECTOR_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_BATCH_ID_VALUE
  }

  private val sequence: Int by lazy {
    intent.getIntExtra(
      AppSettings.Constants.VECTOR_SEQUENCE_KEY,
      AppSettings.Constants.SEQUENCE_ZERO_VALUE
    )
  }

  override val vm by lazy {
    AddVectorViewModel(
      vectorId,
      App.getSurveysRepository(),
      App.getPreferenceProvider(),
      App.getLocalPhotoRepository()
    )
  }

  private val photoSheetBehavior by lazy {
    BottomSheetBehavior.from(photoSheet)
  }

  private val networkUtils by lazy {
    App.getNetworkUtils()
  }

//  private val broadcastReceiver by lazy {
//    object: BroadcastReceiver() {
//      override fun onReceive(context: Context?, intent: Intent?) {
//        if(networkUtils.isPiSSIDAvailable()) {
//          if (networkUtils.hasConnectedToPi()) networkUtils.connectToPi()
//        }
//      }
//    }
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (vectorId == AppSettings.Constants.NO_ID_VALUE || batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<SurveysActivity>(
        AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_no_surveyId)
      )
    }
    setContentView(R.layout.activity_add_vector_survey)
    setupToolbar(toolbarAV, getString(R.string.title_vector), R.drawable.close_white)
  }

  override fun bind() {
    setupSpinner(this, R.array.mosquitoSpecies, vectorSpecies, vectorSpeciesOtherLayout)
    setupPhotoSheetButton()
    onRequestCameraPermission()
    setDnaRequestListener()
//    registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
  }

  override fun unbind() {
//    unregisterReceiver(broadcastReceiver)
  }

  override fun saveAndClose() {
    val model = getModel()
    doAsync {
      vm.save(model)
      uiThread {
        startActivity<VectorBatchDetailActivity>(
          AppSettings.Constants.BATCH_ID_KEY to batchId
        )
        finish()
      }
    }
  }

  override fun getModel(): Vector {
    val result = Vector()
    result.id = vm.id
    result.batchId = batchId
    result.sequence = sequence
    result.species = getSpinnerValue(vectorSpecies, vectorSpeciesOther, vectorSpeciesOtherLayout)
    result.gender = vectorGender.selectedValue()
    result.stage = vectorStage.selectedValue()
    result.didFeed = vectorDidFeed.asBoolean()
    if (vectorDna.text.toString() != getString(R.string.dna_field_label))
      result.dna = vectorDna.text.toString()
    if (vm.savePhoto()) result.photoPath = vm.getPhotoPath()
    return result
  }

  private fun setupPhotoSheetButton() {
    photoSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    photoButton.setOnClickListener {
      when (photoSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
        true -> photoSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        false -> photoSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  @AfterPermissionGranted(UsesCamera.REQUEST_CODE)
  override fun onRequestCameraPermission() {
    when {
        EffortlessPermissions.hasPermissions(this,
          UsesCamera.CAMERA_PERMISSION,
          UsesCamera.STORAGE_PERMISSION
        ) -> setupPhotoListener()
        EffortlessPermissions.permissionPermanentlyDenied(
          this, UsesCamera.CAMERA_PERMISSION
        )
          or
        EffortlessPermissions.permissionPermanentlyDenied(
          this,
          UsesCamera.STORAGE_PERMISSION
        ) -> {
          fromCamera?.visibility = View.GONE
        }
        else -> askForCameraPermission(this, "Please allow access to the camera")
    }
  }

  @AfterPermissionDenied(UsesCamera.REQUEST_CODE)
  override fun onCameraPermissionDenied() {
    info("Camera permission denied")
    fromCamera?.visibility = View.GONE
  }

  private fun setupPhotoListener() {
    if (fromCamera.visibility != View.GONE) setupCameraListener()
    setupGalleryListener()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        UsesCamera.REQUEST_CODE -> {
          val bitmap = BitmapFactory.decodeFile(vm.getPhotoPath())
          vm.photo = bitmap
          setPhotoToView(bitmap)
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
      showShortMessage(vectorSurveyRoot, "There was an error choosing or taking the photoPath")
    }
  }

  private fun setupCameraListener() {
    fromCamera.setOnClickListener {
      vm.getTempImageFile()?.let {
        takePhotoIntent(it)
      }
    }
  }

  private fun takePhotoIntent(file: File) {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    cameraIntent.resolveActivity(packageManager)?.let {
      photoSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      val photoURI = FileProvider.getUriForFile(
        this,
        "eu.mignot.pathogentracker.fileprovider",
        file
      )
      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
      startActivityForResult(cameraIntent, UsesCamera.REQUEST_CODE)
    }
  }

  private fun setupGalleryListener() {
    fromGallery.setOnClickListener {
      photoSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
      galleryIntent.type = "image/*"
      startActivityForResult(galleryIntent, UsesGallery.REQUEST_CODE)
    }
  }

  private fun setPhotoToView(bmp: Bitmap) {
    photoView.setImageBitmap(bmp)
    photoView.visibility = View.VISIBLE
    photoButton.setText(R.string.action_replace_photo)
  }

  private fun setDnaRequestListener() {
    vectorDna.setOnClickListener {
      showShortMessage(vectorSurveyRoot, "This hasn't been implemented yet")
    }
  }

//  private fun getDnaData() {
//    showShortMessage(vectorSurveyRoot, "getting dna data...")
//  }
}
