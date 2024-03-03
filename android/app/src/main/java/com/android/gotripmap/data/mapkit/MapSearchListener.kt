package com.android.gotripmap.data.mapkit

import android.util.Log
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error

class MapSearchListener(
  private val searchAgain: (pointObject: GeoObject) -> Unit
): Session.SearchListener {
  override fun onSearchResponse(response: Response) {
    val results = response.collection.children.take(2)
    results.forEach {
      Log.w("intermediate","search_started")
      searchAgain(it.obj!!)
    }
  }

  override fun onSearchError(p0: Error) {
    Log.w("endLocation", "problem")
  }
}
