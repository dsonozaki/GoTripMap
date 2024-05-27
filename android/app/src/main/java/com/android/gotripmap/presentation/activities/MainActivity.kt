package com.android.gotripmap.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.gotripmap.data.mapkit.MapSearchListener
import com.android.gotripmap.domain.entities.MyPoint
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.presentation.screens.MainScreen
import com.android.gotripmap.ui.theme.GoTripMapTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Snippet
import java.lang.Double.min
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MapKitFactory.initialize(this)

    setContent {
      GoTripMapTheme {
        MainScreen()
      }
    }
  }

  override fun onStart() {
    MapKitFactory.getInstance().onStart()
    super.onStart()
  }

  override fun onStop() {
    MapKitFactory.getInstance().onStop()
    super.onStop()
  }
}
