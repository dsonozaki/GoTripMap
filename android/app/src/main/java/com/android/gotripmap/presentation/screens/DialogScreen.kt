package com.android.gotripmap.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.presentation.bottom_navigation.BottomItem
import com.android.gotripmap.presentation.elements.BobaDialogElement
import com.android.gotripmap.presentation.elements.FloatingButtons
import com.android.gotripmap.presentation.elements.UserInputDialogFragment
import com.android.gotripmap.presentation.utils.textSaver
import com.android.gotripmap.presentation.viewmodels.DialogVM
import com.android.gotripmap.ui.theme.AppTheme
import com.android.gotripmap.ui.theme.gradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun DialogScreen(navHostController: NavHostController) {
  val text = rememberSaveable(saver = textSaver()) { mutableStateOf(TextFieldValue("")) }
  var visibility by rememberSaveable { mutableStateOf(false) }
  val focusRequester = remember {
    FocusRequester()
  }
  val viewModel = getViewModel<DialogVM>()
  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()
  val speech = viewModel.speech.collectAsState()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(gradient)
  ) {
    Column(
      Modifier
        .padding(17.dp, 30.dp, 17.dp, 0.dp)
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(scrollState)
    ) {

      BobaDialogElement(text = stringResource(id = R.string.hello))
      BobaDialogElement(text = stringResource(id = R.string.choose_route))
      UserInputDialogFragment(text, focusRequester) {
        visibility = true
        coroutineScope.launch {
          delay(550)
          scrollState.animateScrollTo(scrollState.maxValue)
        }
      }
      AnimatedVisibility(visible = visibility) {
        BobaDialogElement(text = stringResource(id = R.string.choose_transport))
      }
    }
    AnimatedVisibility(visible = visibility) {
      ChooseTransport {
        viewModel.addEntry(text.value.text, LocalDateTime.now(), it)
        navHostController.navigate(BottomItem.SEARCH_SCREEN)
      }
    }
    FloatingButtons(
      Modifier
        .padding(17.dp, 86.dp, 17.dp, 30.dp)
        .fillMaxWidth()
        .height(50.dp),
      navHostController,
      focusRequester,
      {
        viewModel.startListening(Locale.getDefault().language)
      },
      {
        viewModel.stopListening()
      },
      speech,
      text
    )
  }
}

@Composable
fun ChooseTransport(submitRequest: (Transport) -> Unit) {
  Row(
    modifier = Modifier
      .padding(17.dp, 0.dp, 17.dp, 0.dp)
      .fillMaxWidth()
      .height(82.dp), horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
      TransportButton(name = stringResource(R.string.walking)) {
        submitRequest(Transport.WALKING)
      }
      TransportButton(name = stringResource(R.string.bus)) {
        submitRequest(Transport.PUBLIC)
      }
    }
    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
      TransportButton(name = stringResource(R.string.car)) {
        submitRequest(Transport.CAR)
      }
      TransportButton(name = stringResource(R.string.bicycle)) {
        submitRequest(Transport.BICYCLE)
      }
    }
  }
}

@Composable
fun TransportButton(name: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .size(155.dp, 30.dp)
      .clip(RoundedCornerShape(20.dp))
      .background(AppTheme.copy(alpha = 0.7f))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(text = name, style = MaterialTheme.typography.bodyLarge)
  }
}

