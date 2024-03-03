package com.android.gotripmap.domain.entities

import com.yandex.mapkit.geometry.Point

data class RouteIntermediateResults(
  val startAddress: String,
  val startPlace: String,
  var endAddress: String = "",
  var endPlace: String = "",
  var points: List<Point>
)
