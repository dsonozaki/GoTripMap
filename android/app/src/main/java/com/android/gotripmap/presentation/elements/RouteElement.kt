package com.android.gotripmap.presentation.elements

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.presentation.maps.OpenMaps
import com.android.gotripmap.presentation.utils.rightSubstring
import com.android.gotripmap.ui.theme.AppTheme
import com.android.gotripmap.ui.theme.GreyInApp

/**
 * Отрисовка элемента маршрута
 */
@Composable
fun RouteElement(route: Route, modifier: Modifier = Modifier, onLike: (Int) -> Unit) {
  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  Box(
    modifier = modifier
      .padding(Dp(0f), Dp(0f), Dp(0f), Dp(20f))
      .clip(RoundedCornerShape(Dp(10f)))
      .background(Color.White)
      .animateContentSize() //анимируем изменение размера контейнера при отображении дополнительных данных
  ) {
    Log.w("elementUpdate",route.route)
    Column {
      Box(
        modifier = modifier
          .aspectRatio(1.777f)
          .background(GreyInApp)
          .clickable(onClick = {
            expanded = !expanded
          }),
        contentAlignment = Alignment.Center
      ) {
        VisibleInfoRoute(route, onLike, modifier)
      }
      AnimatedVisibility(visible = expanded) {
        AdditionalInfoRoute(route)
      }
    }
  }
}

/**
 * Основная часть карточки маршрута
 */

@Composable
private fun VisibleInfoRoute(
  route: Route,
  onLike: (Int) -> Unit,
  modifier: Modifier
) {
  SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(route.imageLink)
      .build(),
    loading = { CircularProgressIndicator(color = AppTheme) },
    error = { ErrorElement() },
    contentDescription = "image of the place",
    contentScale = ContentScale.Crop
  )
  val imageId = if (route.liked) {
    R.drawable.mdi_heart_liked
  } else {
    R.drawable.mdi_heart
  }
  Column(
    Modifier
      .fillMaxWidth()
      .padding(Dp(9f), Dp(4f), Dp(6f), Dp(5f)),
    horizontalAlignment = Alignment.End
  ) {
    Image(
      ImageVector.vectorResource(id = imageId),
      contentDescription = "like",
      modifier = Modifier
        .size(Dp(24f), Dp(24f))
        .clickable { onLike(route.id) },
    )
    Spacer(modifier = Modifier.weight(1f))
    RouteRow(
      modifier = modifier,
      color = Color.White,
      startPoint = route.startPointAddress,
      endPoint = route.endPointPlace,
      length = route.length
    )
  }
}

/**
 * Скрытая часть карточки маршрута
 */
@Composable
fun AdditionalInfoRoute(route: Route) {
  Column(modifier = Modifier.padding(Dp(10f), Dp(8f), Dp(8f), Dp(15f))) {
    Row {
      Text(
        text = stringResource(id = R.string.route_time, route.timeRequired),
        color = GreyInApp,
        style = MaterialTheme.typography.bodySmall
      )
      Spacer(modifier = Modifier.weight(1f))
      ImageByTransport(route.transport,false)
    }
    Text(
      //      text = "${route.startPointAddress.rightSubstring()}: ${route.startPointPlace.rightSubstring()}\n${route.endPointAddress.rightSubstring()}: ${route.endPointPlace.rightSubstring()}",
      text = route.startPointAddress,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(Dp(0f), Dp(10f), Dp(0f), Dp(0f))
    )
    Text(
      //      text = "${route.startPointAddress.rightSubstring()}: ${route.startPointPlace.rightSubstring()}\n${route.endPointAddress.rightSubstring()}: ${route.endPointPlace.rightSubstring()}",
      text = route.endPointPlace,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(Dp(0f), Dp(0f), Dp(0f), Dp(10f))
    )
    Spacer(modifier = Modifier.weight(1f))
    Row(modifier = Modifier.align(Alignment.End)) {
      val context = LocalContext.current
      val openMaps = OpenMaps(context) //инъекию сделать, возможно
      ElevatedButton(
        onClick = {
          openMaps.openMaps(
            route.route,
            route.transport
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = AppTheme, contentColor = Color.Black)
      ) {
        Text(stringResource(id = R.string.start_text_button), style =  MaterialTheme.typography.labelLarge)
      }
    }
  }
}
