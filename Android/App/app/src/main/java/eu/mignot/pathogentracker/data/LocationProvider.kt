package eu.mignot.pathogentracker.data

import eu.mignot.pathogentracker.surveys.data.models.ui.UiLocation
import io.reactivex.Observable

interface LocationProvider {
  fun getLocation(): Observable<UiLocation>
}
