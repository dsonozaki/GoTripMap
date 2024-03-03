package com.android.gotripmap.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.gotripmap.presentation.screens.MainScreen
import com.android.gotripmap.ui.theme.GoTripMapTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GoTripMapTheme {
        MainScreen()
      }
    }
  }
}
