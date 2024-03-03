package com.android.gotripmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkLocationPermission(): Boolean {
  val permissionGranted = PackageManager.PERMISSION_GRANTED
  return ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_COARSE_LOCATION
  ) == permissionGranted && ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
  ) == permissionGranted
}
