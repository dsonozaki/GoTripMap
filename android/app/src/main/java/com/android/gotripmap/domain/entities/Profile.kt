package com.android.gotripmap.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
  val id: Int = 0,
  val username: String = "",
  val phone: String = "",
  val email: String = "",
  val photo: String = "",
  val hash: String = "",
  val initialized: Boolean = false
)
