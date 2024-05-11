package com.android.gotripmap.data.mapkit

import android.location.Geocoder
import android.util.Log
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.data.utilities.getAddress
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions

class SearchMap(
  private val response: RouteResponse,
  private val startPoint: MyAddress,
  private val entry: SearchEntry,
  private val callback: (
    length: String, time: String, intermediateResults: RouteIntermediateResults
  ) -> Unit
) {
  private val length = response.entries.size
  private val searchManager =
    SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
  private val searchOptions = SearchOptions()

  init {
    Log.w("index", "length=$length")
      search(
        RouteIntermediateResults(
          points = listOf(startPoint.coordinate),
          startAddress = startPoint.address!!,
          startPlace = startPoint.address
        ), startPoint.coordinate, response.entries[0].destpoint.category
      )
  }

  private fun search(
    intermediateResults: RouteIntermediateResults,
    startPoint: Point,
    endPoint: String,
    index: Int = 0
  ) {
    val searcher = MapSearchListener {
      val results = intermediateResults.copy()
      val points = results.points.toMutableList()
      val newPoint = it.geometry[0].point!!
      points.add(newPoint)
      results.points = points
      if (results.points.size < length + 1) {
        val newIndex = index+1
        search(
          results.copy(),
          newPoint,
          response.entries[newIndex].destpoint.category,
          newIndex
        )
      } else if (results.points.size == length + 1) {
        results.endAddress = it.name.toString() //geocoder.getAddress(newPoint).toString() пофиксить
        results.endPlace = it.name.toString()
        sendPoints(results)
      }
    }
    searchManager.submit(
      endPoint,
      Geometry.fromPoint(startPoint),
      searchOptions,
      searcher
    )
  }

  private fun sendPoints(intermediateResults: RouteIntermediateResults) {
    when (entry.transport) {
      Transport.CAR -> RouteCarMap(
        intermediateResults, callback
      )

      Transport.WALKING -> {
        RoutePedMap(intermediateResults, callback)
      }
      Transport.PUBLIC -> {
        RouteCarMap(
          intermediateResults, callback
        ) //пофиксить как можно быстрее
       // RoutePublicMap(intermediateResults, callback)
      }
      Transport.BICYCLE -> {
        RouteBikeMap(intermediateResults, callback)
      }
    }
  }
}
