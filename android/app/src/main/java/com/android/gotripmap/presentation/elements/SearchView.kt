package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.gotripmap.R
import com.android.gotripmap.presentation.bottom_navigation.BottomItem

@Composable
fun SearchView(
  text: String,
  navigateToDialog: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(0.dp, 0.dp, 0.dp, 18.dp)
      .defaultMinSize(minHeight = 30.dp)
      .clip(RoundedCornerShape(30.dp))
      .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp))
      .background(Color.White)
      .clickable {
        navigateToDialog()
      },
    contentAlignment = Alignment.Center
  ) {
    Row(
      Modifier
        .fillMaxWidth()
        .padding(8.dp, 0.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        modifier = Modifier.weight(1f), text = text,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Image(
        ImageVector.vectorResource(R.drawable.material_symbols_search),
        contentDescription = "search",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
          .size(Dp(20f), Dp(20f)),
      )
    }
  }
}
