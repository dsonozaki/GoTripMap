package com.android.gotripmap.data.pojo

data class RouteResponse(val id: String, val entries: List<Entry>)

data class Entry(
  val entryno: Int,
  val time: Int,
  val destpoint: DestPoint
)

data class DestPoint(
  val category: String
)
