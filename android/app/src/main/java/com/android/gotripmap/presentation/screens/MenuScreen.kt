package com.android.gotripmap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.gotripmap.R
import com.android.gotripmap.ui.theme.GradientStart
import com.android.gotripmap.ui.theme.gradient

/**
 * Экран меню пользователя
 */
@Composable
fun MenuScreen(navController: NavController) {
  var tabIndex by rememberSaveable { mutableIntStateOf(0) }

  val tabs = listOf(R.string.history_title, R.string.liked_title)

  Column(modifier = Modifier.fillMaxWidth().background(gradient)) {
    Spacer(modifier = Modifier.height(27.dp))
    TabRow(backgroundColor = Color.Unspecified,
      contentColor = Color.Black,
      selectedTabIndex = tabIndex) {
      tabs.forEachIndexed { index, title ->
        Tab(text = { Text( text=stringResource(title), style = MaterialTheme.typography.titleSmall) },
          selected = tabIndex == index,
          onClick = { tabIndex = index }
        )
      }
    }
    when (tabIndex) {
      0 -> HistoryScreen(navController)
      1 -> LikedScreen()
    }
  }
}

