package com.android.gotripmap.data.mappers

import android.util.Log
import com.android.gotripmap.data.db.RouteDbModel
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.google.gson.Gson

/**
 * Маппер для маршрута
 */
class RouteMapper(private val gson: Gson) {

  private val links = mapOf(
    "бар" to "https://img.buzzfeed.com/buzzfeed-static/static/2023-01/6/16/asset/c6d7aaed38aa/sub-buzz-472-1673023003-6.jpg",
    "кафе" to "https://www.thespruceeats.com/thmb/zhMaTeUN1CS4nfqCh6WLSeToUWk=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/churros-and-hot-chocolate-831371188-5aa0574c303713003790d039.jpg",
    "аптека" to "https://www.artefact.com//wp-content/uploads/2023/08/GettyImages-1248493102-1200x675.jpg",
    "кино" to "https://ichef.bbci.co.uk/news/800/cpsprodpb/13CD0/production/_95340118_popcorn4.jpg",
    "default" to "https://wanderhealthy.com/wp-content/uploads/2023/11/Roadmap-to-health5.jpg",
    "лес" to "https://attunedneurofeedback.com/wp-content/uploads/2023/02/If-A-Tree-Falls-in-the-Forest-And-No-One-Is-There-To-Hear-It.jpg",
    "парикмахерская" to "https://tengrinews.kz/userdata/news/2020/news_400783/thumb_m/photo_319005.jpeg",
    "поликлиника" to "https://www.cnet.com/a/img/resize/15413fdbcf8ee39cb5a185861709dfdaf8b35a11/hub/2021/08/27/c5c07796-40b6-473d-9cc1-42289c9d3454/gettyimages-900075200.jpg?auto=webp&fit=crop&height=675&width=1200",
    "больница" to "https://www.cnet.com/a/img/resize/15413fdbcf8ee39cb5a185861709dfdaf8b35a11/hub/2021/08/27/c5c07796-40b6-473d-9cc1-42289c9d3454/gettyimages-900075200.jpg?auto=webp&fit=crop&height=675&width=1200",
    "музей" to "https://idporcelaine.net/wp-content/uploads/2022/08/quelle-est-la-difference-entre-une-porcelaine-et-une-ceramique-1200x765.jpg",
    "mc donalds" to "https://img.gazeta.ru/files3/260/15168260/3i6a0919-hdr-pic4_zoom-1500x1500-72639.jpg",
    "парк" to "https://i.pinimg.com/originals/0c/a6/8a/0ca68a9eb3c56f061050460e95a246ad.jpg",
    "ресторан" to "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/go-to-facebook-friends-event-1539894402.jpg",
    "магазин" to "https://newyorkfolk.com/wp-content/uploads/2023/05/1684512767_0x0-1000x600.jpg",
    "театр" to "https://sun9-73.userapi.com/impg/hOCBaCgM2BEAjAxVNsIlj3cy8XK4VyPtX_8eEA/jwx9qzXKfm4.jpg?size=1280x712&quality=95&sign=e0763c9ece4435e4ffd9a426114d0d5f&c_uniq_tag=D0qGc1sHxx4gM3rjMq0KCoRXuk96erwk06EJYsbeoLA&type=album"
  )

  private fun getImage(str: String): String {
    return if (links.containsKey(str)) {
      links[str]!!
    } else {
      links["default"]!!
    }
  }

  private fun mapDbToDtModel(routeDbModel: RouteDbModel) =
    Route(
      id = routeDbModel.id,
      length = routeDbModel.length,
      route = routeDbModel.route,
      endPointPlace = routeDbModel.endPointPlace,
      endPointAddress = routeDbModel.endPointAddress,
      imageLink = routeDbModel.imageLink,
      timeRequired = routeDbModel.timeRequired,
      startPointAddress = routeDbModel.startPointAddress,
      startPointPlace = routeDbModel.startPointPlace,
      transport = routeDbModel.transport,
      searchEntry = routeDbModel.searchEntry,
      liked = routeDbModel.liked
    )

  private fun mapDtToDbModel(route: Route): RouteDbModel =
    RouteDbModel(
      id = route.id,
      length = route.length,
      route = route.route,
      endPointPlace = route.endPointPlace,
      endPointAddress = route.endPointAddress,
      imageLink = route.imageLink,
      timeRequired = route.timeRequired,
      startPointAddress = route.startPointAddress,
      startPointPlace = route.startPointPlace,
      transport = route.transport,
      searchEntry = route.searchEntry,
      liked = route.liked
    )

  fun mapDtListToDbList(dtList: List<Route>): List<RouteDbModel> =
    dtList.map { mapDtToDbModel(it) }


  fun mapApiToDbModel(
    length: String,
    time: String,
    routeIntermediateResults: RouteIntermediateResults,
    searchEntry: SearchEntry,
    response: RouteResponse
  ): RouteDbModel {
    Log.w("route_points", "start")
    routeIntermediateResults.points.forEach {
      Log.w("route_points", "lattitude ${it.latitude}")
      Log.w("route_points", "longitude ${it.longitude}")
    }
    return RouteDbModel(
      length = length,
      startPointPlace = routeIntermediateResults.startPlace,
      route = gson.toJson(routeIntermediateResults.points),//изменить
      startPointAddress = routeIntermediateResults.startAddress,
      endPointPlace = routeIntermediateResults.endPlace,
      endPointAddress = routeIntermediateResults.endAddress,
      imageLink = getImage(response.entries.random().destpoint.category),
      //imageLink = response.entries.random().img, правильная версия
      timeRequired = time,
      transport = searchEntry.transport,
      searchEntry = searchEntry.id
    )
  }

  fun mapDbListToDtList(dbList: List<RouteDbModel>): List<Route> =
    dbList.map { mapDbToDtModel(it) }

}
