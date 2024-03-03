package com.android.gotripmap.data.mapkit

import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error

class RouteCarMap(
  private val intermediateResults: RouteIntermediateResults,
  private val callback: (
    length: String, time: String, intermediateResults: RouteIntermediateResults
  ) -> Unit
) : DrivingSession.DrivingRouteListener {

  init {
    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
    val drivingOptions = DrivingOptions()
    val vehicleOptions = VehicleOptions()
    val requestPoints: List<RequestPoint> =
      intermediateResults.points.map { RequestPoint(it, RequestPointType.WAYPOINT, null, null) }
    drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
  }

  override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
    val route = routes.first()
    callback(route.metadata.weight.distance.text,route.metadata.weight.time.text, intermediateResults)

  }

  override fun onDrivingRoutesError(p0: Error) {
    TODO("Not yet implemented")
  }
}
