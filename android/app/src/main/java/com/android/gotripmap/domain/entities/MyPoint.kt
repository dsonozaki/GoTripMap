package com.android.gotripmap.domain.entities

import com.yandex.mapkit.geometry.Point

data class MyPoint(val pointData: Point, val url: String?,  val address: String?, var previous: MyPoint?=null, var optimalDistance: Double = Double.MAX_VALUE, var timeDeviation: Int=Int.MAX_VALUE)
