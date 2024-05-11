package com.android.gotripmap.presentation.states

import androidx.compose.ui.text.input.TextFieldValue
import com.android.gotripmap.domain.entities.Profile

data class ProfileState(
  var id: Int = 0,
  var username: TextFieldValue = TextFieldValue(""),
  var phone: TextFieldValue = TextFieldValue(""),
  var email: TextFieldValue = TextFieldValue(""),
  var photo: String = "",
  var hash: String = "",
  var initialized: Boolean? = null
) {
  constructor (profile: Profile) : this(
    profile.id,
    TextFieldValue(text = profile.username),
    TextFieldValue(text = profile.phone),
    TextFieldValue(text = profile.email),
    profile.photo,
    profile.hash,
    profile.initialized
  )

  fun toProfile(): Profile = Profile(
    this.id,
    this.username.text,
    this.phone.text,
    this.email.text,
    this.photo,
    this.hash,
    this.initialized?:false
  )
}
