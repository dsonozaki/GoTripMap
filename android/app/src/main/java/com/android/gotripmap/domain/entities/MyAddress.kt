package com.android.gotripmap.domain.entities

import com.yandex.mapkit.geometry.Point

data class MyAddress(val coordinate: Point = Point(0.0,0.0),val address: String? = null)
