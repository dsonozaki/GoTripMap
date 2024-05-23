package com.android.gotripmap.domain.entities

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.transport.masstransit.Route

data class RouteIntermediateResults(
  val startAddress: String,
  val startPlace: String,
  var endAddress: String = "",
  var endPlace: String = "",
  var points: List<Point>,
  var imageUrl: String = "",
  var time: Double = 0.0,
  var distance: Double = 0.0
)
