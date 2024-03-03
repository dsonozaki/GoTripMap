package com.android.gotripmap.data.mapkit

import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error

class RouteBikeMap(
  private val intermediateResults: RouteIntermediateResults,
  private val callback: (
    length: String, time: String, intermediateResults: RouteIntermediateResults
  ) -> Unit
) : Session.RouteListener {

  init {
    val bicycleRouter = TransportFactory.getInstance().createBicycleRouterV2()
    val timeOptions = TimeOptions()
    val requestPoints: List<RequestPoint> =
      intermediateResults.points.map { RequestPoint(it, RequestPointType.WAYPOINT, null, null) }
    bicycleRouter.requestRoutes(requestPoints, timeOptions, this)
  }

  override fun onMasstransitRoutes(routes: MutableList<Route>) {
    val route = routes.first()
    callback(route.metadata.weight.walkingDistance.text,route.metadata.weight.time.text, intermediateResults)

  }

  override fun onMasstransitRoutesError(p0: Error) {
    TODO("Not yet implemented")
  }


}
