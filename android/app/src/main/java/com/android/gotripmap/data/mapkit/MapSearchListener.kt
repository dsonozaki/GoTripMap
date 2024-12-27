package com.android.gotripmap.data.mapkit

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error

class MapSearchListener(
  private val searchAgain: (pointObjects: List<GeoObject>) -> Unit
): Session.SearchListener {
  override fun onSearchResponse(response: Response) {
    val results = response.collection.children.map { it.obj!! }
    searchAgain(results)
  }

  override fun onSearchError(p0: Error) {
  }
}
