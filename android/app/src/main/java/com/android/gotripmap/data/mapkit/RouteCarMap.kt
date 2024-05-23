package com.android.gotripmap.data.mapkit

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error

class RouteCarMap(
  private val startPoint: Point,
  private val endPoint: GeoObject,
  private val callback: (
    length: Double, time: Double, point: GeoObject
  ) -> Unit
) : DrivingSession.DrivingRouteListener {


  init {
    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
    val drivingOptions = DrivingOptions()
    val vehicleOptions = VehicleOptions()
    val startRequestPoint = RequestPoint(startPoint,RequestPointType.WAYPOINT,null,null)
    val endRequestPoint = RequestPoint(endPoint.geometry[0].point!!,RequestPointType.WAYPOINT,null,null)
    drivingRouter.requestRoutes(listOf(startRequestPoint, endRequestPoint), drivingOptions, vehicleOptions, this)
  }

  override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
    val route = routes.first()
    callback(route.metadata.weight.time.value,route.metadata.weight.distance.value,endPoint)
  }

  override fun onDrivingRoutesError(p0: Error) {
    TODO("Not yet implemented")
  }
}
