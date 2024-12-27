package com.android.gotripmap.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.android.gotripmap.R
import com.android.gotripmap.checkLocationPermission
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.presentation.elements.LocationElement
import com.android.gotripmap.presentation.elements.RouteElement
import com.android.gotripmap.presentation.viewmodels.SearchScreenVM
import com.android.gotripmap.ui.theme.AppTheme
import com.android.gotripmap.ui.theme.gradient
import org.koin.androidx.compose.getViewModel

/**
 * Экран для отображения результатов текущего запроса
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(navigateToDialog: () -> Unit) {

  val viewModel = getViewModel<SearchScreenVM>()
  val currentEntry by viewModel.currentEntry.collectAsState()
  val location by viewModel.locationText.collectAsState(null)
  val routes by viewModel.routes.collectAsState()
  val sent = rememberSaveable {
    mutableStateOf(false)
  }
  val like = remember {{ id: Int -> viewModel.changeLiked(id) }}
  val loadData = remember {{ viewModel.loadData() }}
  val getLocation = remember {{ viewModel.getLocation() }}

  if (currentEntry != null && routes!!.isEmpty() && location!=null) {
    if (!sent.value) {
      loadData()
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
          getLocation()
        }
      })
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
      val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_START) {
          if (context.checkLocationPermission()) {
            getLocation()
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
      LocationElement(location = location, currentEntry = currentEntry?.entry ?: stringResource(R.string.search_placeholder), transport = currentEntry?.transport?:Transport.PUBLIC, navigateToDialog = navigateToDialog, transportUndefined=currentEntry?.transport==null)
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(Modifier.fillMaxSize()) {
          itemsIndexed(
            items = routes ?: listOf(),
            key = { _, item -> item.id },
          ) { _, item ->

            RouteElement(route = item, modifier = Modifier.animateItemPlacement()) {
                id: Int -> viewModel.changeLiked(id)
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
