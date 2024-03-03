package com.android.gotripmap.presentation.utils

fun String.rightSubstring(): String {
  if (this.length > 25) {
    return this.substring(0,25) + "..."
  }
  return this
}
