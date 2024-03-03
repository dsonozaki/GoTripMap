package com.android.gotripmap.presentation.bottom_navigation

import com.android.gotripmap.R

/**
 * Элементы главного меню
 */
sealed class BottomItem(val iconId: Int,val route: String) {
  object Menu: BottomItem(R.drawable.menu,MENU_SCREEN)
  object Search: BottomItem(R.drawable.search,SEARCH_SCREEN)
  object Profile: BottomItem(R.drawable.profile,EDIT_PROFILE_SCREEN)

  companion object {
    const val MENU_SCREEN = "menu_screen"
    const val SEARCH_SCREEN = "search_screen"
    const val EDIT_PROFILE_SCREEN = "profile_screen"
    const val DIALOG_SCREEN = "dialog_screen"
    const val LOGIN_SCREEN = "login_screen"
  }
}
