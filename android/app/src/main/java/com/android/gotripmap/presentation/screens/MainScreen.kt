package com.android.gotripmap.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.android.gotripmap.presentation.bottom_navigation.BottomNavigation
import com.android.gotripmap.presentation.bottom_navigation.AppNavHost
import com.android.gotripmap.presentation.viewmodels.MainVM
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

/**
 * Главный экран и навигация в нём
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {

  val navController = rememberNavController()
  val snackBarHostState = remember {
    SnackbarHostState()
  }
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val viewModel = getViewModel<MainVM>()
  val status = viewModel.status.collectAsState(initial = null)

  Scaffold(bottomBar = {
    BottomNavigation(navController = navController)
  },
    snackbarHost = { SnackbarHost(snackBarHostState) }) {
    if (status.value!=null) {
      LaunchedEffect(key1 = status) {
        coroutineScope.launch {
          snackBarHostState.showSnackbar(
            message = status.value!!.toString(context),
            duration = SnackbarDuration.Long
          )
        }
      }
    }
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
