package com.android.gotripmap.data.pojo

data class RouteResponse(val id: String, val entries: List<Entry>)

data class Entry(
  val entryno: Int,
  val tsys: String,
  val time: Int,
  val startpoint: String,
  val destpoint: DestPoint,
  val img: String
)

data class DestPoint(
  val name: String,
  val category: String
)
