package com.android.gotripmap.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.android.gotripmap.presentation.elements.RouteElement
import com.android.gotripmap.presentation.viewmodels.LikedVM
import org.koin.androidx.compose.getViewModel

/**
 * Экран с понравившимися маршрутами
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikedScreen() {
  val viewModel = getViewModel<LikedVM>()
  val data by viewModel.likedItems.collectAsState()

  LazyColumn(Modifier.fillMaxSize().padding(Dp(10f),Dp(35f),Dp(10f),Dp(0f))) {
    itemsIndexed(
      items = data,
      key = { _, item -> item.id },
    ) {_, item ->
      RouteElement(route=item, modifier = Modifier.animateItemPlacement()) {id ->
        viewModel.changeLiked(id)
      }
    }
  }
}
