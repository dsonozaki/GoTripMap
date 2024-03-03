package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.MyAddress
import kotlinx.coroutines.flow.Flow

interface PiLocationManager {
  fun locationUpdates(intervalInMillis: Long): Flow<MyAddress>
}
