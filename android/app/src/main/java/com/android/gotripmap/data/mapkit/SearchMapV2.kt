package com.android.gotripmap.data.mapkit

import android.os.Handler
import android.os.Looper
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.MyPoint
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessPhotoObjectMetadata
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Snippet
import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.thread
import kotlin.math.abs

class SearchMapV2(
  response: RouteResponse,
  private val startPoint: MyAddress,
  private val entry: SearchEntry,
  private val callback: (
    intermediateResults: RouteIntermediateResults
  ) -> Unit
) {
  private val searchOptions =
    SearchOptions().setSnippets(Snippet.PHOTOS.value).setResultPageSize(50)
  private val EARTH_RADIUS_KM = 6371.01
  private val PEDESTRIAN_SPEED = 1.388
  val searchManager =
    SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

  init {
    val keywords = response.entries.map { it.destpoint.category }
    val timeRequirements = listOf(0) + response.entries.map { it.time }
    thread {
      val startPoint = MyPoint(startPoint.coordinate, null, null, null, 0.0, 0)
      search(
        mutableListOf(listOf(startPoint)),
        mutableListOf(startPoint),
        startPoint,
        keywords,
        timeRequirements,
        0
      )
    }
  }

  private fun findDistance(point1: Point, point2: Point): Double {
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

  private fun findOptimalPathsForPedestrians(
    startPoints: List<MyPoint>, nextPoints: List<MyPoint>,
    greedyDistance: Double, timeLimitation: Int
  ): List<MyPoint> {
    for (startPoint in startPoints) {
      if (startPoint.optimalDistance > greedyDistance) {
        continue
      }
      for (nextPoint in nextPoints) {
        val distance = findDistance(startPoint.pointData, nextPoint.pointData)
        val time = distance * PEDESTRIAN_SPEED
        val newDeviation = abs(time.toInt() - timeLimitation * 60)
        if (newDeviation < nextPoint.timeDeviation) {
          nextPoint.optimalDistance = startPoint.optimalDistance + distance
          nextPoint.timeDeviation = newDeviation
          nextPoint.previous = startPoint
        }
      }
    }
       var result = nextPoints.filter { it.timeDeviation < timeLimitation * 6 }
    if (result.isEmpty()) {
      result = listOf(nextPoints.minBy { it.timeDeviation })
    }
    return result
  }

  private fun findShortestPaths(
    startPoints: List<MyPoint>, nextPoints: List<MyPoint>,
    greedyDistance: Double
  ): List<MyPoint> {
    for (startPoint in startPoints) {
      if (startPoint.optimalDistance > greedyDistance) {
        continue
      }
      for (nextPoint in nextPoints) {
        val newOptimalDistance =
          startPoint.optimalDistance + findDistance(startPoint.pointData, nextPoint.pointData)
        if (newOptimalDistance < nextPoint.optimalDistance) {
          nextPoint.optimalDistance = newOptimalDistance
          nextPoint.previous = startPoint
        }
      }
    }
    return nextPoints
  }

  private fun findShortestPath(
    pointsData: List<List<MyPoint>>, greedyDistance: Double,
    timeLimitation: List<Int>
  ): List<MyPoint> {
    var myPointsList: List<MyPoint> = pointsData[1]
    for (i in 2 until pointsData.size) {
      val currentTimeLimitation = timeLimitation[i]
      myPointsList = if (currentTimeLimitation == 0) {
        findShortestPaths(myPointsList, pointsData[i], greedyDistance)
      } else {
        if (entry.transport == Transport.WALKING) {
          findOptimalPathsForPedestrians(
            myPointsList,
            pointsData[i],
            greedyDistance,
            currentTimeLimitation
          )
        } else {
          pointsData[i]
        }
      }
    }
    var minimumPoint = myPointsList.minBy { it.optimalDistance }
    val result = mutableListOf<MyPoint>()
    result.add(minimumPoint)
    while (minimumPoint.previous != null) {
      minimumPoint = minimumPoint.previous!!
      result.add(minimumPoint)
    }
    return result.reversed()
  }


  private fun searchMinDistanceFirst(startPoint: MyPoint, points: List<MyPoint>): List<MyPoint> {
    points.forEach {
      it.optimalDistance =
        startPoint.optimalDistance + findDistance(startPoint.pointData, it.pointData)
      it.previous = startPoint
    }
    return points
  }

  private fun searchBestTimeMatchFirst(
    startPoint: MyPoint,
    points: List<MyPoint>,
    timeRequirement: Int
  ): List<MyPoint> {
    val result = mutableListOf<MyPoint>()
    val synchronisedQueue = ArrayBlockingQueue<List<MyPoint>>(1)
    var calculated = 0
    points.forEach {
      calculatePathParameters(
        listOf(
          startPoint,
          it
        )
      ) { time: Double, distance: Double, pointList: List<MyPoint> ->
        val newDeviation = abs(time.toInt() - timeRequirement * 60)
        pointList[1].optimalDistance = pointList[0].optimalDistance + distance
        pointList[1].timeDeviation = newDeviation
        pointList[1].previous = startPoint
        result.add(pointList[1])
        calculated++
        if (calculated == points.size) {
          synchronisedQueue.put(result)
        }
      }
    }
    val results = synchronisedQueue.take()
    return results
  }

  private fun greedySearchForTopResults(
    startPoint: MyPoint,
    pointsData: List<List<MyPoint>>
  ): List<MyPoint> {
    var initPoint = startPoint
    val result = mutableListOf(initPoint)
    for (i in 1 until pointsData.size) {
      initPoint = pointsData[i][0]
      result += initPoint
    }
    return result
  }

  private fun createIntermediateResult(route: List<MyPoint>) {
    val lastPoint = route.last()
    calculatePathParameters(route) { time: Double, distance: Double, points: List<MyPoint> ->
      callback(
        RouteIntermediateResults(
          points = route.map { it.pointData },
          startAddress = startPoint.address!!,
          startPlace = startPoint.address,
          imageUrl = lastPoint.url!!,
          endAddress = lastPoint.address!!,
          endPlace = lastPoint.address,
          time = time,
          distance = distance
        )
      )
    }
  }

  private fun calculatePathParameters(
    route: List<MyPoint>,
    continuation: (time: Double, distance: Double, points: List<MyPoint>) -> Unit
  ) {
    when (entry.transport) {
      Transport.CAR ->
        RouteCarMap(
          route, continuation
        )

      Transport.WALKING ->
        RoutePedMap(
          route, continuation
        )

      Transport.PUBLIC ->

        RoutePublicMap(
          route, continuation
        )


      Transport.BICYCLE ->
        RouteBikeMap(
          route, continuation
        )
    }
  }

  private fun search(
    intermediateResults: MutableList<List<MyPoint>>,
    path: MutableList<MyPoint>,
    nextPoint: MyPoint,
    keywords: List<String>,
    timeRequirements: List<Int>,
    index: Int
  ) {
    val searchListener = MapSearchListener { points ->
      thread {
        var myPoints = if (index == keywords.lastIndex) {
          points.map {
            MyPoint(
              it.geometry[0].point!!, it.metadataContainer.getItem(
                BusinessPhotoObjectMetadata::class.java
              )?.photos?.get(0)?.id?.format(
                "XXL"
              ) ?: "", it.name
            )
          }
        } else {
          points.map { MyPoint(it.geometry[0].point!!, null, null) }
        }
        var startPoint = nextPoint
        if (timeRequirements[index + 1] == 0) {
          myPoints = searchMinDistanceFirst(path[index], myPoints)
          path.add(myPoints.minBy { it.optimalDistance })
        } else {
          myPoints = searchBestTimeMatchFirst(path[index], myPoints, timeRequirements[index + 1])
          path.add(myPoints.minBy { it.timeDeviation })
        }
        if (entry.transport == Transport.WALKING) {
          if (index == 0) {
            if (timeRequirements[1] != 0) {
              var firstLayerPoints = myPoints.filter { it.timeDeviation < timeRequirements[1] * 6 }
              if (firstLayerPoints.isEmpty()) {
                firstLayerPoints = listOf(myPoints.minBy { it.timeDeviation })
              }
              intermediateResults.add(firstLayerPoints)
            } else {
              intermediateResults.add(myPoints)
            }
          } else {
            intermediateResults.add(myPoints)
          }
        } else {
          if (timeRequirements[index + 1] != 0 || (timeRequirements[index + 1] == 0 && index < timeRequirements.lastIndex - 1 && timeRequirements[index + 2] != 0)) {
            intermediateResults.add(listOf(path[index + 1]))
            startPoint = path[index + 1]
          } else {
            intermediateResults.add(myPoints)
          }
        }
        if (index == keywords.lastIndex) {
          val topResult = greedySearchForTopResults(intermediateResults[0][0], intermediateResults)
          if (index <= 1) {
            createIntermediateResult(path)
          } else {
            val distance = path.last().optimalDistance
            if (timeRequirements.all { it == 0 }) {
              for (i in 1 until intermediateResults.size) {
                intermediateResults[i] = intermediateResults[i].filter {
                  findDistance(
                    intermediateResults[0][0].pointData,
                    it.pointData
                  ) < distance
                }
              }
            }
            val pathResult =
              findShortestPath(intermediateResults, distance, timeRequirements)
            createIntermediateResult(pathResult)
          }
          createIntermediateResult(topResult)
        } else {
          search(intermediateResults, path, startPoint, keywords, timeRequirements, index + 1)
        }
      }
    }
    Handler(Looper.getMainLooper()).post {
      searchManager.submit(
        keywords[index],
        Geometry.fromPoint(nextPoint.pointData),
        searchOptions,
        searchListener
      )
    }
  }
}
