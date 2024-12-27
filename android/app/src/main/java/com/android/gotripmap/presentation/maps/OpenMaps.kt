package com.android.gotripmap.presentation.maps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.android.gotripmap.domain.entities.Transport
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.mapkit.geometry.Point
import java.lang.reflect.Type


/**
 * Класс для открытия маршрута в картах гугла и яндекса
 */
class OpenMaps(private val context: Context) {

  /**
   * Проверка, установлено ли приложение карт
   */
  private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
      packageManager.getPackageInfo(packageName, 0)
      true
    } catch (e: PackageManager.NameNotFoundException) {
      false
    }
  }

  /**
   * Функция для получения ссылки для интента в зависимости от карты
   */
  private fun getMapLink(
    points: List<Point>,
    transport: Transport,
    mapType: Maps
  ): Uri {
    val link = when (mapType) {
      is Maps.YandexMaps -> "yandexmaps://maps.yandex.ru/?rtext=" + points.joinToString("~") { "${it.latitude}%2C${it.longitude}" } + "&rtt=" + when (transport) {
        Transport.WALKING -> "pd"
        Transport.BICYCLE -> "bc"
        Transport.CAR -> "auto"
        Transport.PUBLIC -> "mt"
      }

      Maps.GoogleMaps -> {
        val startPoint = points[0]
        val endPoint = points[points.lastIndex]
        "https://www.google.com/maps/dir/?api=1&origin=${startPoint.latitude},${startPoint.longitude}&destination=${endPoint.latitude},${endPoint.longitude}&travelmode=" + when (transport) {
          Transport.WALKING -> "walking"
          Transport.BICYCLE -> "bicycling"
          Transport.CAR -> "driving"
          Transport.PUBLIC -> "transit"
        } + if (points.size > 2) {
          "&waypoints=" + points.subList(1, points.lastIndex).joinToString("%7C") {
            "${it.latitude},${it.longitude}"
          }
        } else {
          ""
        }
      }
    }
    return Uri.parse(link)
  }

  /**
   * Функция для создания диалога с пользователем при открытии маршрута
   */
  fun openMaps(
    route: String,
    transport: Transport
  ) {
    val pm = context.packageManager
    val intents = mutableListOf<Intent>()
    val listMaps = listOf(Maps.GoogleMaps, Maps.YandexMaps)
    for (app in listMaps) {
      if (!isPackageInstalled(app.pack, pm)) {
        continue
      }
      val listType: Type = object : TypeToken<ArrayList<Point?>?>() {}.type
      val targetIntent =
        Intent(Intent.ACTION_VIEW, getMapLink(Gson().fromJson(route, listType), transport, app))
      targetIntent.setPackage(app.pack)
      intents.add(targetIntent)
    }
    if (intents.isEmpty()) {
      //Добавить обработку возможной ошибки
      return
    }
    val chooserIntent =
      Intent.createChooser(intents.removeAt(0), "Select app to share")
    chooserIntent.putExtra(
      Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray()
    )
    context.startActivity(chooserIntent)
  }
}
