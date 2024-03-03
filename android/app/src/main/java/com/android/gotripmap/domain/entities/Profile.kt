package com.android.gotripmap.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
  val username: String = "John Doe",
  val phone: String = "",
  val email: String = "",
  val receiveEmails: Boolean = false,
  val photo: String = "",
  val initialized: Boolean = false
)
