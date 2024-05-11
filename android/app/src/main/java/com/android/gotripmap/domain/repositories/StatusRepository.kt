package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface StatusRepository {
  val isConnected: Flow<Boolean>
  val statusChannel: Channel<Status?>
  suspend fun setStatus(status: Status?)
  suspend fun handle(e: Exception)
}
