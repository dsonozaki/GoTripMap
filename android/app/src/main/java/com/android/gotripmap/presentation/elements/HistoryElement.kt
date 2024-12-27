package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.ui.theme.GreyInApp

/**
 * Отрисовка элемента запроса
 */
@Composable
fun HistoryElement(
  searchEntry: SearchEntry,
  modifier: Modifier = Modifier,
  onClick: (Int) -> Unit
) {
  Row(
    modifier
      .fillMaxWidth()
      .clickable { onClick(searchEntry.id) }
  ) {
    Column(modifier.padding(Dp(20F), Dp(0f), Dp(13f), Dp(21f))) {
      Text(
        modifier = Modifier.padding(Dp(0F), Dp(22f), Dp(0f), Dp(10f)),
        text = searchEntry.dateTime,
        color = Color.Black,
        style = MaterialTheme.typography.bodyMedium
      )
      EntryRow(modifier, searchEntry)
      RouteRow(
        modifier.padding(Dp(0f), Dp(10F), Dp(0F), Dp(0F)),
        Color.Black,
        searchEntry.startPointPlace?: stringResource(R.string.unknown),
        searchEntry.endPointPlace?: stringResource(R.string.unknown),
        searchEntry.length?: "??? m"
      )
    }
  }
  val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
  Canvas(
    Modifier
      .fillMaxWidth()
      .height(1.dp)
  ) {

    drawLine(
      color = GreyInApp,
      start = Offset(0f, 0f),
      end = Offset(size.width, 0f),
      pathEffect = pathEffect
    )
  }
}

@Composable
private fun EntryRow(
  modifier: Modifier,
  searchEntry: SearchEntry
) {
  Row(
    modifier=modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      modifier = Modifier
        .padding(Dp(0F), Dp(0f), Dp(16f), Dp(0f))
        .weight(1f),
      text = searchEntry.entry,
      color = Color.Black,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    ImageByTransport(searchEntry.transport,false)
  }

}

