package com.android.gotripmap.domain.entities

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Point

data class MapKitResult(val time: Double,val length: Double,val geoObject: GeoObject)
