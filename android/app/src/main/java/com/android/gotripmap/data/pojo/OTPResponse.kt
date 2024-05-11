package com.android.gotripmap.data.pojo

import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.SearchEntry

data class OTPResponse(val profile: Profile, val entries: List<SearchEntry>, val routes: List<Route>)
