package com.android.gotripmap.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.gotripmap.presentation.screens.MainScreen
import com.android.gotripmap.ui.theme.GoTripMapTheme
import com.yandex.mapkit.MapKitFactory

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
