package com.android.gotripmap.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.gotripmap.R
import com.android.gotripmap.presentation.bottom_navigation.BottomItem
import com.android.gotripmap.presentation.elements.ProfileSettingElement
import com.android.gotripmap.presentation.elements.ReceiveCodeElement
import com.android.gotripmap.presentation.utils.EmailPhoneCorrectChecker
import com.android.gotripmap.presentation.utils.textSaver
import com.android.gotripmap.presentation.viewmodels.EditProfileVM
import com.android.gotripmap.ui.theme.AppTheme
import com.android.gotripmap.ui.theme.GreyInApp
import com.android.gotripmap.ui.theme.gradient
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(navHostController: NavHostController) {
  val viewModel = getViewModel<EditProfileVM>()
  val profile = viewModel.profile.collectAsState()
  val receivingCode = rememberSaveable {
    mutableStateOf(false)
  }
  val buttonActive = rememberSaveable {
    mutableStateOf(false)
  }
  val code = rememberSaveable(saver= textSaver()) {
    mutableStateOf(TextFieldValue(""))
  }
  val requiredCode = viewModel.codeFlow.collectAsState()
  val context = LocalContext.current

  if (requiredCode.value != "") {
    LaunchedEffect(key1 = requiredCode) {
      Toast.makeText(context, requiredCode.value, Toast.LENGTH_LONG).show()
    }
  }


  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(gradient)
  ) {
    Column(
      modifier = Modifier
        .padding(10.dp, 0.dp)
        .fillMaxHeight(), verticalArrangement = Arrangement.SpaceAround
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Image(
            painterResource(R.drawable.app_icon),
            contentDescription = "App icon",
            Modifier.size(60.dp)
          )
          Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
          )
        }
      }
      Column(
        Modifier
          .fillMaxWidth()
      ) {
        AnimatedVisibility(
          visible = !receivingCode.value
        ) {
          ProfileSettingElement(
            true,
            stringResource(R.string.email_or_phone),
            profile.value.phone
          ) {
            viewModel.updatePhone(it)
            val checker = EmailPhoneCorrectChecker(it.text)
            if (!receivingCode.value) {
              buttonActive.value = checker.isCorrectEmail() || checker.isCorrectPhone()
            }
          }
        }
        AnimatedVisibility(
          visible = receivingCode.value
        ) {
          ReceiveCodeElement(code,buttonActive)
        }
        ElevatedButton(
          onClick = {
            if (!receivingCode.value) {
              receivingCode.value = true
              buttonActive.value = false
              viewModel.startAuth()
            } else {
              viewModel.startOTP(code.value.text)
              navHostController.navigate(BottomItem.EDIT_PROFILE_SCREEN)
            }
          },
          enabled = buttonActive.value,
          modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 30.dp, 0.dp, 0.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme,
            contentColor = Color.Black,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = GreyInApp
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(stringResource(R.string.enter), style = MaterialTheme.typography.bodyLarge)
        }
      }
      Box(
        Modifier
          .fillMaxWidth(), contentAlignment = Alignment.CenterStart
      ) {
        //FAQ()
      }
    }
  }
}
