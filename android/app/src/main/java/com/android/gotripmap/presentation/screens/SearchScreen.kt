package com.android.gotripmap.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.FloatingWindow
import androidx.navigation.NavHostController
import com.android.gotripmap.R
import com.android.gotripmap.checkLocationPermission
import com.android.gotripmap.data.mapkit.SearchMap
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.presentation.bottom_navigation.BottomItem
import com.android.gotripmap.presentation.elements.ImageByTransport
import com.android.gotripmap.presentation.elements.RouteElement
import com.android.gotripmap.presentation.viewmodels.SearchScreenVM
import com.android.gotripmap.ui.theme.AppTheme
import com.android.gotripmap.ui.theme.gradient
import com.yandex.mapkit.geometry.Point
import org.koin.androidx.compose.getViewModel

/**
 * Экран для отображения результатов текущего запроса
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(navHostController: NavHostController) {

  val viewModel = getViewModel<SearchScreenVM>()
  val currentEntry by viewModel.currentEntry.collectAsState()
  val location by viewModel.location.collectAsState()
  val routes by viewModel.routes.collectAsState()
  val sent = rememberSaveable {
    mutableStateOf(false)
  }

  if (currentEntry != null && routes!!.isEmpty() && location.coordinate.latitude != 0.0) {
    Log.w("request", "${location.coordinate.longitude} ${currentEntry!!.entry}")
    if (!sent.value) {
      viewModel.loadData()
      sent.value = true
    }
  }



  Column(
    Modifier
      .fillMaxWidth()
      .background(gradient)
  ) {
    val locationPermissions = arrayOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestMultiplePermissions(),
      onResult = { permissions ->
        val permissionsGranted = if (permissions.isEmpty()) {
          false
        } else {
          permissions.values.reduce { acc, isPermissionGranted ->
            acc && isPermissionGranted
          }
        }
        if (permissionsGranted) {
          viewModel.getLocation()
        }
      })
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
      val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_START) {
          if (context.checkLocationPermission()) {
            viewModel.getLocation()
          } else {
            locationPermissionLauncher.launch(locationPermissions)
          }
        }
      }
      lifecycleOwner.lifecycle.addObserver(observer)
      onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
      }
    }
    )
    Column(Modifier.padding(10.dp, 17.dp, 10.dp, 0.dp)) {
      SearchView(
        currentEntry?.entry ?: stringResource(R.string.search_placeholder),
        navHostController
      )
      Row(Modifier.padding(0.dp, 0.dp, 0.dp, 13.dp)) {
        Image(
          painterResource(R.drawable.mdi_map_marker_outline),
          contentDescription = "geotag",
          contentScale = ContentScale.FillBounds,
          modifier = Modifier
            .size(Dp(20f), Dp(20f))
        )
        Text(
          text = location.address
            ?: stringResource(id = R.string.address_unknown),  //долго грузит, кэшировать
          color = Color.Black,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
        ImageByTransport(currentEntry?.transport)
      }
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(Modifier.fillMaxSize()) {
          itemsIndexed(
            items = routes ?: listOf(),
            key = { _, item -> item.id },
          ) { _, item ->
            RouteElement(route = item, modifier = Modifier.animateItemPlacement()) { id ->
              viewModel.changeLiked(id)
            }
          }
        }
        androidx.compose.animation.AnimatedVisibility(visible = routes?.isEmpty() ?: false) {
          CircularProgressIndicator(color = AppTheme)
        }
        androidx.compose.animation.AnimatedVisibility(visible = routes == null) {
          Text(
            text = stringResource(id = R.string.create_first_request),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}

@Composable
fun SearchView(
  text: String,
  navHostController: NavHostController
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
        navHostController.navigate(BottomItem.DIALOG_SCREEN)
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
        painterResource(R.drawable.material_symbols_search),
        contentDescription = "search",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
          .size(Dp(20f), Dp(20f)),
      )
    }
  }
}
