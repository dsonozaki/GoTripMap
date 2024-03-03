package com.android.gotripmap.presentation.utils

class EmailPhoneCorrectChecker(val text: String) {
  fun isCorrectPhone() =
    text.matches(Regex("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}\$"))

  fun isCorrectEmail() =
    text.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))
}
