package com.android.gotripmap.data.repositories

import android.content.Context
import android.net.ConnectivityManager
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.Status
import com.android.gotripmap.domain.repositories.StatusRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.net.SocketTimeoutException

class StatusRepositoryImpl(
  context: Context
) : StatusRepository {

  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private val _isConnected = MutableStateFlow(false)
  override val statusChannel: Channel<Status?> = Channel()
  override suspend fun setStatus(status: Status?) {
    statusChannel.send(status)
  }

  override val isConnected: Flow<Boolean> = _isConnected

  init {
    // Observe network connectivity changes
    connectivityManager.registerDefaultNetworkCallback(object :
      ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: android.net.Network) {
        _isConnected.value = true
      }
      override fun onLost(network: android.net.Network) {
        _isConnected.value = false
      }
    })
  }
  override suspend fun handle(e: Exception) {
    val status = if (e is HttpException) {
       when(e.code()) {
        403 -> Status(R.string.access_denied)
        500 -> Status(R.string.server_error)
        404 -> Status(R.string.server_not_found)
        else -> Status(R.string.error_unknown, e.message())
      }
    }
    else if (e is SocketTimeoutException){
      Status(R.string.cant_connect_to_server)
    }
    else {
      Status(R.string.error_unknown, e.message?:"")
    }
    statusChannel.send(status)
  }
}
