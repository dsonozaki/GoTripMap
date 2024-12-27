package com.android.gotripmap.presentation.activities

import android.app.Activity
import android.os.Bundle
import com.android.gotripmap.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class MapKitActivity: Activity() {
  private lateinit var mapview: MapView
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onStop() {
    mapview.onStop()
    MapKitFactory.getInstance().onStop()
    super.onStop()
  }

  override fun onStart() {
    mapview.onStart()
    MapKitFactory.getInstance().onStart()
    super.onStart()
  }


}
