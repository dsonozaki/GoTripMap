package com.android.gotripmap.data.mapkit

import android.util.Log
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.domain.entities.MapKitResult
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessPhotoObjectMetadata
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Snippet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.math.abs


class SearchMap(
  private val response: RouteResponse,
  private val startPoint: MyAddress,
  private val entry: SearchEntry,
  private val callback: (
    intermediateResults: RouteIntermediateResults
  ) -> Unit
) {
  private val length = response.entries.size
  private val coroutineScope = CoroutineScope(Dispatchers.Default)
  private val searchManager =
    SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
  private val searchOptions = SearchOptions().setSnippets(Snippet.PHOTOS.value)

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
    Log.w("index", index.toString())
    val searcher = MapSearchListener {
      val timeLimit = response.entries[index].time * 60
      Log.w("timeLimit", timeLimit.toString())
      findPoint(
        startPoint,
        it,
        timeLimit
      ) { searchResults: List<MapKitResult> ->
        searchResults.forEach {
          Log.w("result", it.toString())
          val results = intermediateResults.copy()
          val newPoint = it.geoObject.geometry[0].point!!
          val points = results.points.toMutableList()
          points.add(newPoint)
          results.points = points
          results.distance += it.length
          results.time += it.time
          if (results.points.size < length + 1) {
            val newIndex = index + 1
            search(
              results.copy(),
              newPoint,
              response.entries[newIndex].destpoint.category,
              newIndex
            )

          } else if (results.points.size == length + 1) {
            results.endAddress =
              it.geoObject.name.toString() //toFix
            results.endPlace = it.geoObject.name.toString()
            results.imageUrl =
              it.geoObject.metadataContainer.getItem(BusinessPhotoObjectMetadata::class.java).photos[0].id.format(
                "XXL"
              )
            callback(results)
          }
        }
      }
    }
    searchManager.submit(
      endPoint,
      Geometry.fromPoint(startPoint),
      searchOptions,
      searcher
    )
  }

  private fun findPoint(
    startPoint: Point,
    endPoints: List<GeoObject>,
    timeLimit: Int,
    continuation: (results: List<MapKitResult>) -> Unit
  ) {
    var i = 0
    var resultTime = Double.MAX_VALUE
    var resultDistance = 0.0
    var resultObject: GeoObject? = null
    val results = mutableListOf<MapKitResult>()
    val callback = if (timeLimit == 0) {
      val first = endPoints[0].geometry[0].point!!
      { time: Double, length: Double, geoObject: GeoObject ->
        Log.w("building","finished")
        val point = geoObject.geometry[0].point!!
        if (point.latitude == first.latitude && point.longitude == first.longitude) {
          results.add(MapKitResult(time, length, geoObject))
        } else if (time < resultTime) {
          resultTime = time
          resultDistance = length
          resultObject = geoObject
        }
        if (i == (endPoints.size - 1)) {
          if ((resultObject != null) && (resultObject?.name != endPoints[0].name)) {
            results.add(MapKitResult(resultTime, resultDistance, resultObject!!))
          }
          continuation(results.toList())
        }
        i += 1
      }
    } else {
      var compareTime = Double.MAX_VALUE
      { time: Double, length: Double, geoObject: GeoObject ->
        if (abs(timeLimit - time) < compareTime) {
          compareTime = abs(timeLimit - time)
          resultTime = time
          resultDistance = length
          resultObject = geoObject
        }
        if (i == endPoints.size - 1) {
          continuation(listOf(MapKitResult(resultTime, resultDistance, resultObject!!)))
        }
        i += 1
      }
    }
    when (entry.transport) {
      Transport.CAR ->
        for (endPoint in endPoints) {
          RouteCarMap(
            startPoint, endPoint, callback
          )
        }

      Transport.WALKING ->
        for (endPoint in endPoints) {
          RoutePedMap(
            startPoint, endPoint, callback
          )
        }

      Transport.PUBLIC ->
        for (endPoint in endPoints) {
          RoutePublicMap(
            startPoint, endPoint, callback
          )
        }


      Transport.BICYCLE ->
        for (endPoint in endPoints) {
          RouteBikeMap(
            startPoint, endPoint, callback
          )
        }
    }
  }
}
