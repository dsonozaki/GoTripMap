package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.Transport

@Composable
fun LocationElement(location: String?, currentEntry: String, transport: Transport, transportUndefined: Boolean, navigateToDialog: () -> Unit) {
  Column {
    SearchView(
      currentEntry,
      navigateToDialog
    )
    Row(Modifier.padding(0.dp, 0.dp, 0.dp, 13.dp)) {
      Image(
        ImageVector.vectorResource(R.drawable.mdi_map_marker_outline),
        contentDescription = "geotag",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
          .size(Dp(20f), Dp(20f))
      )
      Text(
        text = location
          ?: stringResource(id = R.string.address_unknown),  //долго грузит, кэшировать
        color = Color.Black,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f)
      )
      ImageByTransport(transport,transportUndefined)
    }
  }
}
