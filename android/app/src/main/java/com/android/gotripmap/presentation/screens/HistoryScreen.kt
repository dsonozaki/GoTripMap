package com.android.gotripmap.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.android.gotripmap.presentation.bottom_navigation.BottomItem
import com.android.gotripmap.presentation.elements.HistoryElement
import com.android.gotripmap.presentation.viewmodels.HistoryVM
import org.koin.androidx.compose.getViewModel

/**
 * Экран с историей запросов
 */
@Composable
fun HistoryScreen(navController: NavController) {
  val viewModel = getViewModel<HistoryVM>()
  val data by viewModel.requestsHistory.collectAsState()

  LazyColumn(
    Modifier
      .fillMaxSize()
      .padding(Dp(0f), Dp(25f), Dp(0f), Dp(0f))) {
    items(data) {
      HistoryElement(searchEntry = it) {id ->
        viewModel.makeEntryCurrent(id)
        navController.navigate(BottomItem.SEARCH_SCREEN)
      }
    }
  }
}
