package com.android.gotripmap.data.mapkit

import android.os.Handler
import android.os.Looper
import com.android.gotripmap.domain.entities.MyPoint
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.runtime.Error
import kotlin.concurrent.thread

class RouteCarMap(
  private val points: List<MyPoint>,
  private val callback: (
    length: Double, time: Double, points: List<MyPoint>
  ) -> Unit
) : DrivingSession.DrivingRouteListener {


  init {
    val requestPoints = points.map {  RequestPoint(it.pointData,RequestPointType.WAYPOINT,null,null) }
    Handler(Looper.getMainLooper()).post {
      val drivingOptions = DrivingOptions()
      val vehicleOptions = VehicleOptions()
      val drivingRouter =
        DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
      drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }
  }

  override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
    val route = routes.first()
    val time = route.metadata.weight.time.value
    val distance = route.metadata.weight.distance.value
    thread {
      callback(time, distance, points)
    }
  }

  override fun onDrivingRoutesError(p0: Error) {
    TODO("Not yet implemented")
  }
}
