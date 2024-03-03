package com.android.gotripmap.presentation.maps

sealed class Maps(val pack: String) {
  object YandexMaps: Maps("ru.yandex.yandexmaps")
  object GoogleMaps: Maps("com.google.android.apps.maps")

}
