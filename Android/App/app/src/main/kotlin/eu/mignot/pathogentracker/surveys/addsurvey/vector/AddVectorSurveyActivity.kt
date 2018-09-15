package eu.mignot.pathogentracker.surveys.addsurvey.vector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.surveys.addsurvey.BaseSurveyActivity
import eu.mignot.pathogentracker.surveys.surveydetail.VectorBatchDetailActivity
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.*
import eu.mignot.pathogentracker.util.AppSettings.Constants.FILEPROVIDER_AUTHORITY
import kotlinx.android.synthetic.main.activity_add_vector_survey.*
import kotlinx.android.synthetic.main.vector_form.*
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File

class AddVectorSurveyActivity: BaseSurveyActivity<Vector>(), UsesCamera {

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
      App.getLocalSurveysRepository(),
      App.getPreferenceProvider(),
      App.getLocalPhotoRepository(),
      App.getTempFileProvider()
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // If no vector id provided or no batch id provided
    if (vectorId == AppSettings.Constants.NO_ID_VALUE || batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<SurveysActivity>(
        AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_no_surveyId)
      )
    }
    setContentView(R.layout.activity_add_vector_survey)
    setupToolbar(toolbarAV, getString(R.string.title_vector), R.drawable.close_white)
  }

  override fun bind() {
    setupSpinner(R.array.mosquitoSpecies, vectorSpecies, vectorSpeciesOtherLayout)
    onRequestCameraPermission()
  }

  /**
   * @see BaseSurveyActivity.unbind
   */
  override fun unbind() {}

  /**
   * @see BaseSurveyActivity.saveAndClose
   */
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

  /**
   * @see BaseSurveyActivity.getModel
   */
  override fun getModel(): Vector {
    val result = Vector()
    result.id = vm.id
    result.batchId = batchId
    result.sequence = sequence
    result.species = getSpinnerValue(vectorSpecies, vectorSpeciesOther, vectorSpeciesOtherLayout)
    result.gender = vectorGender.selectedValue()
    result.stage = vectorStage.selectedValue()
    result.didFeed = vectorDidFeed.asBoolean()
    if (vm.photo != null) result.photo = savePhoto()
    return result
  }

  /**
   * Creates a Photo object and
   * requests vm to save the bitmap
   */
  private fun savePhoto(): Photo {
    return with (Photo()) {
      fileName = File(vm.getPhotoPath()).name
      parentId = vm.id
      path = vm.getPhotoPath()!!
      vm.savePhoto(this)
      this
    }
  }

  /**
   * @see UsesCamera.onRequestCameraPermission
   */
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
          photoButton?.visibility = View.GONE
        }
        else -> askForCameraPermission(this, "Please allow access to the camera")
    }
  }

  /**
   * @see UsesCamera.onCameraPermissionDenied
   */
  @AfterPermissionDenied(UsesCamera.REQUEST_CODE)
  override fun onCameraPermissionDenied() {
    info("Camera permission denied")
    photoButton?.visibility = View.GONE
  }

  /**
   * Checks if user has camera permissions
   * and takes a photo, if not, asks for permission
   */
  private fun setupPhotoListener() {
    photoButton?.setOnClickListener {
      if (EffortlessPermissions.hasPermissions(
          this, UsesCamera.CAMERA_PERMISSION, UsesCamera.STORAGE_PERMISSION
        )) {
        vm.getTempImageFile()?.let { f -> takePhotoIntent(f) }
          ?: showShortMessage(vectorSurveyRoot, "Unable to store a temporary file")
      } else {
        askForCameraPermission(this, "Please allow access to the camera")
      }
    }
  }

  /**
   * @see Activity.onActivityResult
   *
   * Handles the response from the camera Intent
   */
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == UsesCamera.REQUEST_CODE) {
        val bitmap = BitmapFactory.decodeFile(vm.getPhotoPath())
        vm.photo = bitmap
        setPhotoToView(bitmap)
      }
    } else {
      showShortMessage(vectorSurveyRoot, "No Photo Selected")
    }
  }

  /**
   * Construct the camera intent and starts it
   */
  private fun takePhotoIntent(file: File) {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    cameraIntent.resolveActivity(packageManager)?.let {
      val photoURI = FileProvider.getUriForFile(
        this,
        FILEPROVIDER_AUTHORITY,
        file
      )
      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
      startActivityForResult(cameraIntent, UsesCamera.REQUEST_CODE)
    }
  }

  private fun setPhotoToView(bmp: Bitmap) {
    photoView.setImageBitmap(bmp)
    photoView.visibility = View.VISIBLE
    photoButton.setText(R.string.action_replace_photo)
  }

  /**
   * Setup the spinner for the mosquito species dropdown
   *
   * Contains an [EditText] component that is hidden and
   * shown if "other" is selected
   */
  private fun setupSpinner(
    arraySource: Int,
    spinnerField: Spinner,
    otherField: TextInputLayout
  ) {
    val adapter = ArrayAdapter.createFromResource(this, arraySource, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinnerField.adapter = adapter
    spinnerField.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos).toString()
        when(selected) {
          "Other" -> {toggleOtherField(true, otherField)}
          else -> {toggleOtherField(field = otherField)}
        }
      }
      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
  }

  /**
   * Retrieve either the selected value or
   * the value of the "other" text field
   */
  private fun getSpinnerValue(spinner: Spinner, textField: EditText, textLayout: TextInputLayout): String {
    return if (textLayout.visibility == View.VISIBLE) {
      textField.text.toString()
    } else {
      val selected = spinner.getItemAtPosition(spinner.selectedItemPosition)
      selected.toString()
    }
  }

  /**
   * Toggles the display of the "other" text field
   */
  private fun toggleOtherField(visible: Boolean = false, field: TextInputLayout) = if (visible) {
    field.visibility = View.VISIBLE
  } else {
    field.visibility = View.GONE
  }
}
