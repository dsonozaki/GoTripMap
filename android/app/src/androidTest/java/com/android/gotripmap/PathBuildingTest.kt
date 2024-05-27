package com.android.gotripmap

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.gotripmap.data.mapkit.MapSearchListener
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Snippet
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@RunWith(AndroidJUnit4::class)
class PathBuildingTest {
  private val searchOptions = SearchOptions().setSnippets(Snippet.PHOTOS.value)
  private val point = Point(59.98748975305617,30.34852144436016)


  private fun findDistance(point1: Point, point2: Point): Double {
    return sqrt((point2.longitude - point1.longitude).pow(2)+(point2.latitude - point1.latitude).pow(2))
  }
  @Test
  fun testMain() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    MapKitFactory.setApiKey(BuildConfig.YANDEX_KEY)
    MapKitFactory.initialize(appContext)
    MapKitFactory.getInstance().onStart()
    val searchManager =
      SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    var distance = 0.0
    val parkSearcher = MapSearchListener {parks ->
      val centerSearcher = MapSearchListener { centers ->
        val museumSearcher = MapSearchListener {museums ->
          Log.w("museums",museums.size.toString())
          Log.w("centers",centers.size.toString())
          Log.w("parks",parks.size.toString())
          val nearestPark = parks.minBy { findDistance(point,it.geometry[0].point!!) }.geometry[0].point!!
          distance += findDistance(point,nearestPark)
          val nearestCenter = centers.minBy { findDistance(nearestPark,it.geometry[0].point!!) }.geometry[0].point!!
          distance+=findDistance(nearestPark,nearestCenter)
          val nearestMuseum = centers.minBy { findDistance(nearestCenter,it.geometry[0].point!!) }.geometry[0].point!!
          distance+=findDistance(nearestCenter,nearestMuseum)
          Log.w("distance",distance.toString())
          val museumsFiltered = museums.filter { findDistance(point,it.geometry[0].point!!) < distance }
          Log.w("museums optimized",museumsFiltered.size.toString())
          val parksFiltered = parks.filter { findDistance(point,it.geometry[0].point!!) < distance }
          Log.w("parks optimized",parksFiltered.size.toString())
          val centersFiltered = centers.filter { findDistance(point,it.geometry[0].point!!) < distance }
          Log.w("centers optimized",centersFiltered.size.toString())
        }
        searchManager.submit("музей", Geometry.fromPoint(point),searchOptions,museumSearcher)
      }
      searchManager.submit("торговый центр", Geometry.fromPoint(point),searchOptions,centerSearcher)
    }
    searchManager.submit("парк", Geometry.fromPoint(point),searchOptions,parkSearcher)
  }
}
