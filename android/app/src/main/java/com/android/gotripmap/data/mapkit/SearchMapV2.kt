package com.android.gotripmap.data.mapkit

import android.util.Log
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.MyPoint
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Snippet
import kotlin.math.abs
import kotlin.random.Random

class SearchMapV2(
  private val response: RouteResponse,
  private val startPoint: MyAddress,
  private val entry: SearchEntry,
  private val callback: (
    intermediateResults: RouteIntermediateResults
  ) -> Unit
) {
  private val searchOptions = SearchOptions().setSnippets(Snippet.PHOTOS.value).setResultPageSize(50)
  private val EARTH_RADIUS_KM = 6371.01
  private var counter = 0
  private val keywords = listOf("музей","парк","кафе")

  private fun findDistance(point1: Point, point2: Point): Double {
    counter+=1
    val lat1Rad: Double = Math.toRadians(point1.latitude)
    val lon1Rad = Math.toRadians(point1.longitude)
    val lat2Rad = Math.toRadians(point2.latitude)
    val lon2Rad = Math.toRadians(point2.longitude)

    // Разница координат

    // Разница координат
    val deltaLat: Double = lat2Rad - lat1Rad
    val deltaLon = lon2Rad - lon1Rad

    // Формула Haversine

    // Формула Haversine
    val a =
      Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) *
        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    // Расстояние

    // Расстояние
    return EARTH_RADIUS_KM * c
  }

  fun findPathTime(p1: Point,p2: Point): Int {
    return 2
  }

  private fun findShortestPaths(
    startPoints: List<MyPoint>, nextPoints: List<MyPoint>,
    greedyDistance: Double, timeLimitation: Int
  ): List<MyPoint> {
    for (startPoint in startPoints) {
      if (startPoint.minDistance>greedyDistance) {
        continue
      }
      for (nextPoint in nextPoints) {
        val newMinDistance = startPoint.minDistance+findDistance(startPoint.pointData,nextPoint.pointData)
        if (timeLimitation!=0) {
          val newTime = findPathTime(startPoint.pointData,nextPoint.pointData)
          val newDeviation = abs(newTime - timeLimitation*60)
          if (newDeviation< nextPoint.timeDeviation) {
            nextPoint.minDistance=newMinDistance
            nextPoint.timeDeviation = newDeviation
            nextPoint.previous=startPoint
          }
        } else if (newMinDistance<nextPoint.minDistance) {
          nextPoint.minDistance=newMinDistance
          nextPoint.previous=startPoint
        }
      }
    }
    return nextPoints
  }

  private fun findShortestPath(pointsData: List<List<MyPoint>>, greedyDistance: Double,
                               timeLimitation: List<Int>): List<MyPoint> {
    var myPointsList: List<MyPoint> = pointsData[0]
    for (i in 1 until pointsData.size) {
      myPointsList=findShortestPaths(myPointsList,pointsData[i],greedyDistance,timeLimitation[i])
    }
    var minimumPoint = myPointsList.minBy { it.minDistance }
    val result = mutableListOf<MyPoint>()
    result.add(minimumPoint)
    while (minimumPoint.previous!=null) {
      minimumPoint = minimumPoint.previous!!
      result.add(minimumPoint)
    }
    return result.reversed()
  }

  fun greedySearch(startPoint: MyPoint, pointsData: List<List<MyPoint>>): Double {
    var minDistance = 0.0
    var initPoint = startPoint
    for (i in 1 until pointsData.size) {
      val previousPoint = initPoint
      initPoint = pointsData[i].minBy { findDistance(initPoint.pointData,it.pointData) }
      minDistance+=findDistance(previousPoint.pointData,initPoint.pointData)
    }
    return minDistance
  }

  fun search(intermediateResults: MutableList<List<MyPoint>>, index: Int) {
    val searchManager =
      SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    val searchListener = MapSearchListener { points ->
      intermediateResults.add(points.map { MyPoint(it.geometry[0].point!!) })
      if (index==keywords.lastIndex) {
        val greedyResult = greedySearch(intermediateResults[0][0],intermediateResults)
        for (i in 1 until intermediateResults.size) {
          Log.w("startSize","$i ${intermediateResults[i].size}")
          intermediateResults[i] = intermediateResults[i].filter {
            findDistance(intermediateResults[0][0].pointData,it.pointData) < greedyResult
          }
          Log.w("optSize","$i ${intermediateResults[i].size}")
        }
        val path = findShortestPath(intermediateResults,greedyResult, listOf())
        Log.w("resultPath",path.joinToString { "${it.pointData.longitude} ${it.pointData.latitude} +" })
      } else {
        Thread.sleep(200+ Random.nextLong(220L))
        search(intermediateResults, index+1)
      }
    }
    searchManager.submit(keywords[index], Geometry.fromPoint(intermediateResults[0][0].pointData),searchOptions,searchListener)
  }

  fun testMain() {
    Log.w("started","start")
    search(mutableListOf(listOf(MyPoint(startPoint.coordinate,null,null,0.0))),0)
  }
}
