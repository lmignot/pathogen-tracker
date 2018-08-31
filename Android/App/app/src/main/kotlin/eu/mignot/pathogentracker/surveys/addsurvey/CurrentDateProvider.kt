package eu.mignot.pathogentracker.surveys.addsurvey

import java.util.*

interface CurrentDateProvider {

  val currentDate: Calendar

  var date: Calendar
    get() = currentDate
    set(d) = setCurrentDate(d)

  private fun setCurrentDate(d: Calendar?) {
    d?.let {
      currentDate.set(
        d.get(Calendar.YEAR),
        d.get(Calendar.MONTH),
        d.get(Calendar.DATE)
      )
    }
  }
}
