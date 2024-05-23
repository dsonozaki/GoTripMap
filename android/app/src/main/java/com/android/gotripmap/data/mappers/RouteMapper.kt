package com.android.gotripmap.data.mappers

import com.android.gotripmap.data.db.RouteDbModel
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.google.gson.Gson

/**
 * Маппер для маршрута
 */
class RouteMapper(private val gson: Gson) {

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

  private fun formatLength(length: Double): String {
    val km: Int = (length/1000).toInt()
    val meters = (length-km*1000).toInt()
    return if (km!=0) {
       "$km km $meters m"
    } else {
      "$meters m"
    }
  }

  private fun formatTime(time: Double): String {
    val minutes = (time/60).toInt()
    val days = minutes/(60*24)
    val hours = (minutes - days*60*24)/60
    val mins = minutes-hours*60-days*60*24
    return if (days!=0) {
      "$days d $hours h $mins m"
    } else if (hours!=0) {
      "$hours h $mins m"
    } else {
      "$mins m"
    }
  }


  fun mapApiToDbModel(
    routeIntermediateResults: RouteIntermediateResults,
    searchEntry: SearchEntry
  ): RouteDbModel {
    return RouteDbModel(
      length = formatLength(routeIntermediateResults.distance),
      startPointPlace = routeIntermediateResults.startPlace,
      route = gson.toJson(routeIntermediateResults.points),
      startPointAddress = routeIntermediateResults.startAddress,
      endPointPlace = routeIntermediateResults.endPlace,
      endPointAddress = routeIntermediateResults.endAddress,
      imageLink = routeIntermediateResults.imageUrl,
      timeRequired = formatTime(routeIntermediateResults.time),
      transport = searchEntry.transport,
      searchEntry = searchEntry.id
    )
  }

  fun mapDbListToDtList(dbList: List<RouteDbModel>): List<Route> =
    dbList.map { mapDbToDtModel(it) }

}
