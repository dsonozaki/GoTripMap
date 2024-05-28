package com.android.gotripmap.presentation.bottom_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.gotripmap.presentation.screens.AuthScreen
import com.android.gotripmap.presentation.screens.DialogScreen
import com.android.gotripmap.presentation.screens.MenuScreen
import com.android.gotripmap.presentation.screens.ProfileScreen
import com.android.gotripmap.presentation.screens.SearchScreen

/**
 * Навигация в приложении
 */
@Composable
fun AppNavHost(
  navHostController: NavHostController
) {
  val navigateToDialog = remember {{ navHostController.navigate(BottomItem.DIALOG_SCREEN) }}

  NavHost(navController = navHostController, startDestination= BottomItem.SEARCH_SCREEN){
    composable(BottomItem.SEARCH_SCREEN) {
      SearchScreen(navigateToDialog)
    }
    composable(BottomItem.MENU_SCREEN) {
      MenuScreen(navHostController)
    }
    composable(BottomItem.EDIT_PROFILE_SCREEN) {
      ProfileScreen(navHostController)
    }
    composable(BottomItem.DIALOG_SCREEN) {
      DialogScreen(navHostController)
    }
    composable(BottomItem.LOGIN_SCREEN) {
      AuthScreen(navHostController)
    }
  }
}
