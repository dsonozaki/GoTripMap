package com.android.gotripmap.domain.entities

data class Route(
  val id: Int = 0,
  val length: String,
  val route: String,
  val startPointPlace: String,
  val startPointAddress: String,
  val endPointPlace: String,
  val endPointAddress: String,
  val imageLink: String,
  val timeRequired: String,
  val transport: Transport,
  val searchEntry: Int,
  val liked: Boolean = false
)
