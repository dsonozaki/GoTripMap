package com.android.gotripmap.domain.usecases.geoposition

import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.repositories.PiLocationManager
import kotlinx.coroutines.flow.Flow

class GetAddressesUseCase(private val locationManager: PiLocationManager) {
  operator fun invoke(interval: Long): Flow<MyAddress> =
    locationManager.locationUpdates(interval)

}
