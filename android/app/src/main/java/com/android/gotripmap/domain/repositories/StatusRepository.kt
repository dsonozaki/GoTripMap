package com.android.gotripmap.domain.repositories

import android.net.ConnectivityManager
import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
  val isConnected: Flow<Boolean>
}
