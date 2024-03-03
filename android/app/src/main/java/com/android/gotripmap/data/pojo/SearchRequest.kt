package com.android.gotripmap.data.pojo

import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Transport


data class SearchRequest(val text: String?, val tsys: String?, val coords: String?) {
  constructor(entry: String, transport: Transport, coords: MyAddress) : this(
    text = entry, tsys = when (transport) {
      Transport.BICYCLE -> "byc"
      Transport.PUBLIC -> "bus"
      Transport.CAR -> "car"
      Transport.WALKING -> "ped"
    },
    coords = "${coords.coordinate.longitude},${coords.coordinate.latitude}"
  )
}
