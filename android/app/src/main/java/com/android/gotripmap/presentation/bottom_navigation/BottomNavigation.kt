package com.android.gotripmap.presentation.bottom_navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.gotripmap.R
import com.android.gotripmap.ui.theme.AppTheme

/**
 * Отрисовка навигации внизу приложения
 */
@Composable
fun BottomNavigation(
  navController: NavController
) {
  val listItems = listOf(BottomItem.Menu, BottomItem.Search, BottomItem.Profile) //список менюшек
  NavigationBar(
    containerColor = Color.Black,
    ) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route //текущий экран
    listItems.forEach { item ->
      BottomNavigationItem(selected = currentRoute == item.route,
        onClick = {
          navController.navigate(item.route)
        },
        icon = {
          Icon(ImageVector.vectorResource(id = item.iconId), contentDescription = "Icon")
        },
        selectedContentColor = AppTheme,
        unselectedContentColor = Color.White)
    } //отрисовываем элементы меню в зависимости от того, выбран элемент или нет
  }
}
