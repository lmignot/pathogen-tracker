package eu.mignot.pathogentracker.viewmodels

import android.graphics.Bitmap
import eu.mignot.pathogentracker.data.models.database.Photo
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.preferences.PreferencesProvider
import eu.mignot.pathogentracker.repository.DevicePhotoRepository
import eu.mignot.pathogentracker.repository.PhotoRepository
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import eu.mignot.pathogentracker.repository.SurveyRepository
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorViewModel
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import io.mockk.*
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.File

class TestAddVectorViewModel {

  companion object {
    const val SURVEY_ID = "V-TEST_VECTOR_ID"
    const val PARENT_ID = "VB-TEST_VECTOR_BATCH_ID"
    const val PHOTO_NAME = "TempFile"
    const val PHOTO_EXT = ".jpg"
    const val MOCK_PATH = "/path/to/app/photos"
  }

  private val dir = File(MOCK_PATH)
  private val surveyRepo: SurveyRepository = spyk(RealmSurveysRepository)
  private val prefsProvider: PreferencesProvider = mockk()
  private val photoRepo: PhotoRepository = spyk(DevicePhotoRepository)
  private val tmpFileProvider: TemporaryFileProvider = spyk(TemporaryFileProvider)


  private val vm by lazy {
    AddVectorViewModel(
      SURVEY_ID, surveyRepo, prefsProvider, photoRepo, tmpFileProvider, dir
    )
  }

  @Before
  fun init() {
    clearMocks(tmpFileProvider, surveyRepo, prefsProvider, photoRepo)
  }

  @Test
  fun `save should call surveys repo`() {
    val survey = with(Vector()) {
      this.id = SURVEY_ID
      this.batchId = PARENT_ID
      this
    }
    every { surveyRepo.storeSurvey(survey) } just Runs
    vm.save(survey)
    verify(atLeast = 1) { surveyRepo.storeSurvey(survey) }
  }

  @Test
  fun `photo path should be null at start`() {
    assertNull(vm.getPhotoPath())
  }

  @Test
  fun `temp image file should call provider`() {
    vm.getTempImageFile(PHOTO_NAME)
    verify(exactly = 1) { tmpFileProvider.getTempFile(PHOTO_NAME, PHOTO_EXT, dir) }
  }

  @Test
  fun `save photo should call repo`() {
    val bmp = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)
    val photo = with(Photo()) {
      this.fileName = "$PHOTO_NAME.$PHOTO_EXT"
      this.path = MOCK_PATH
      this.parentId = SURVEY_ID
      this
    }
    every { prefsProvider.getOptimizeImageRes() } returns false
    every { photoRepo.storePhoto(photo, prefsProvider.getOptimizeImageRes(), bmp) } just Runs
    vm.photo = bmp
    vm.savePhoto(photo)
    verify(exactly = 1) { photoRepo.storePhoto(photo, prefsProvider.getOptimizeImageRes(), bmp) }
  }

}
