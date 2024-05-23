package com.android.gotripmap.data.mapkit

import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error

class RoutePedMap(
  private val startPoint: Point,
  private val endPoint: GeoObject,
  private val callback: (
    length: Double, time: Double, point: GeoObject
  ) -> Unit
): Session.RouteListener {

  init {
    val pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter()
    val timeOptions = TimeOptions()
    val startRequestPoint = RequestPoint(startPoint,RequestPointType.WAYPOINT,null,null)
    val endRequestPoint = RequestPoint(endPoint.geometry[0].point!!,RequestPointType.WAYPOINT,null,null)
    pedestrianRouter.requestRoutes(listOf(startRequestPoint, endRequestPoint), timeOptions, true,this)
  }
  override fun onMasstransitRoutes(routes: MutableList<Route>) {
    val route = routes.first()
    callback(route.metadata.weight.time.value,route.metadata.weight.walkingDistance.value, endPoint)
  }

  override fun onMasstransitRoutesError(p0: Error) {
  }
}
