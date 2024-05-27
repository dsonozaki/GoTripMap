package com.android.gotripmap.domain.entities

import com.yandex.mapkit.geometry.Point

data class MyPoint(val pointData: Point, val url: String?=null,var previous: MyPoint?=null,var minDistance: Double = Double.MAX_VALUE, var timeDeviation: Int=Int.MAX_VALUE)
