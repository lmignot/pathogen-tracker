package eu.mignot.pathogentracker.addsurvey

import eu.mignot.pathogentracker.preferences.AppPreferencesProvider
import eu.mignot.pathogentracker.repository.DevicePhotoRepository
import eu.mignot.pathogentracker.repository.RealmSurveysRepository
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorViewModel
import eu.mignot.pathogentracker.util.TemporaryFileProvider
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.*

class TestAddVectorViewModel {

  private val vm by lazy {
    AddVectorViewModel(
      "V-${UUID.randomUUID()}",
      RealmSurveysRepository,
      AppPreferencesProvider,
      DevicePhotoRepository,
      TemporaryFileProvider
    )
  }

  @Test
  fun get_photo_path_should_be_null_by_default() {
    assertNull(vm.getPhotoPath())
  }

  @Test
  fun get_temp_image_file_should_return_a_file_and_save_the_path() {
    assertNotNull(vm.getTempImageFile())
    assertNotNull(vm.getPhotoPath())
  }

}
