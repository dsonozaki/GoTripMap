package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.Transport

/**
 * Отрисовка транспортного средства в зависимости от выбранного типа маршрута
 */
@Composable
fun ImageByTransport(transport: Transport,transportUndefined: Boolean) {
  val resource = if (transportUndefined) {
    R.drawable.question_mark
  } else {
    when (transport) {
      Transport.PUBLIC -> R.drawable.baseline_directions_bus_24
      Transport.CAR -> R.drawable.baseline_directions_car_24
      Transport.BICYCLE -> R.drawable.baseline_pedal_bike_24
      Transport.WALKING -> R.drawable.ri_walk_line
    }
  }
  Image(
    ImageVector.vectorResource(resource),
    contentDescription = "type of transport",
    contentScale = ContentScale.FillBounds,
    modifier = Modifier
      .size(Dp(20f), Dp(20f))
  )
}
