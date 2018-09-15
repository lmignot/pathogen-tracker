package eu.mignot.pathogentracker.util

import eu.mignot.pathogentracker.App
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object TemporaryFileProvider {

  /**
   * Creates a temporary file and returns it
   *
   * @param id the filename prefix
   * @param ext the file extension
   */
  fun getTempFile(id: String, ext: String): File? {
    val timeStamp = SimpleDateFormat(AppSettings.Constants.PHOTO_TIMESTAMP_FORMAT, Locale.UK)
      .format(Date())
    val fileName = "${id}_$timeStamp"
    return try {
      createTempFile(fileName, ext, App.getDeviceFileDir()).let { it }
    } catch (e: IOException) { null }
  }

}
