package com.android.gotripmap.presentation.states

import androidx.compose.ui.text.input.TextFieldValue
import com.android.gotripmap.domain.entities.Profile

data class ProfileState(
  var username: TextFieldValue = TextFieldValue("John Doe"),
  var phone: TextFieldValue = TextFieldValue(""),
  var email: TextFieldValue = TextFieldValue(""),
  var receiveEmails: Boolean = false,
  var photo: String = "",
  var initialized: Boolean? = null
) {
  constructor (profile: Profile) : this(
    TextFieldValue(text = profile.username),
    TextFieldValue(text = profile.phone),
    TextFieldValue(text = profile.email),
    profile.receiveEmails,
    profile.photo,
    profile.initialized
  )

  fun toProfile(): Profile = Profile(
    this.username.text,
    this.phone.text,
    this.email.text,
    this.receiveEmails,
    this.photo,
    this.initialized?:false
  )
}
