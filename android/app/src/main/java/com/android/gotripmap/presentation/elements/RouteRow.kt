package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.android.gotripmap.R
import com.android.gotripmap.presentation.utils.rightSubstring
import com.android.gotripmap.ui.theme.LightBlue

/**
 * Отрисовка краткого описания маршрута
 */
@Composable
fun RouteRow(modifier: Modifier, color: Color, startPoint: String, endPoint: String, length: String) {
  Row(
    modifier
      .fillMaxWidth()
  ) {
    Text(
      text = startPoint.rightSubstring(),
      color = color,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 1
    )
    Image(
      ImageVector.vectorResource(R.drawable.arrow_1),
      contentDescription = "walking man",
      contentScale = ContentScale.None,
      colorFilter= ColorFilter.tint(color),
      modifier = Modifier
        .align(Alignment.CenterVertically)
        .padding(Dp(9F), Dp(0F), Dp(9F), Dp(0F))
    )
    Text(
      text = endPoint.rightSubstring(),
      color = color,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 1
    )
    Spacer(modifier = Modifier.weight(1f))
    Text(
      text = length,
      color = LightBlue,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 1
    )
  }
}
