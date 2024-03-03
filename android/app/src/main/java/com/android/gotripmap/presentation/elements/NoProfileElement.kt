package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R

@Composable
fun NoProfileElement(circleSize: Dp,imageSize: Dp,modifier: Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(circleSize)) {
      drawCircle(
        color = Color.Black
      )
    }
    Image(
      painterResource(id = R.drawable.no_profile),
      contentDescription = "no_profile",
      modifier = Modifier
        .size(imageSize)
    )
  }
}
