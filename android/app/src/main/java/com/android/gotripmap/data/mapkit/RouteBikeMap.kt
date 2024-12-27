package com.android.gotripmap.data.mapkit

import android.os.Handler
import android.os.Looper
import com.android.gotripmap.domain.entities.MyPoint
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error
import kotlin.concurrent.thread

class RouteBikeMap(
  private val points: List<MyPoint>,
  private val callback: (
    length: Double, time: Double, points: List<MyPoint>
  ) -> Unit
) : Session.RouteListener {

  init {
    val timeOptions = TimeOptions()
    val requestPoints = points.map { RequestPoint(it.pointData,RequestPointType.WAYPOINT,null,null) }
    Handler(Looper.getMainLooper()).post {
      val bicycleRouter = TransportFactory.getInstance().createBicycleRouterV2()
      bicycleRouter.requestRoutes(requestPoints, timeOptions, true, this)
    }
  }

  override fun onMasstransitRoutes(routes: MutableList<Route>) {
    val route = routes.first()
    thread {
      callback(
        route.metadata.weight.time.value,
        route.metadata.weight.walkingDistance.value,
        points
      )
    }
  }

  override fun onMasstransitRoutesError(p0: Error) {
    TODO("Not yet implemented")
  }


}
