package com.android.gotripmap.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.android.gotripmap.presentation.bottom_navigation.BottomNavigation
import com.android.gotripmap.presentation.bottom_navigation.AppNavHost

/**
 * Главный экран и навигация в нём
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {

  val navController = rememberNavController()

  Scaffold(bottomBar = {
    BottomNavigation(navController = navController)
  }) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(
          bottom = it.calculateBottomPadding()
        )
    ) {
      AppNavHost(navHostController = navController)
    }
  }

}
