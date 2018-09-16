package eu.mignot.pathogentracker.util

import java.io.File
import java.io.IOException

object TemporaryFileProvider {

  /**
   * Creates a temporary file and returns it
   *
   * @param fileName the filename
   * @param ext the file extension
   * @param fileDir the path where the file will be stored
   */
  fun getTempFile(fileName: String, ext: String, fileDir: File): File? {
    return try {
      createTempFile(fileName, ext, fileDir).let { it }
    } catch (e: IOException) { null }
  }

}
