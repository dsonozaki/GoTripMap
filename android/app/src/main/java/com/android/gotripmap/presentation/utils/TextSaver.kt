package com.android.gotripmap.presentation.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.text.input.TextFieldValue


fun textSaver() = Saver<MutableState<TextFieldValue>, String>(
  save = { state -> state.value.text },
  restore = { value ->
    mutableStateOf(TextFieldValue(value))
  }
)
