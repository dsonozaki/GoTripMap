package com.android.gotripmap

import android.app.Application
import com.android.gotripmap.di.appModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GoTripMap: Application() {
  override fun onCreate() {
    super.onCreate()
    MapKitFactory.setApiKey(BuildConfig.YANDEX_KEY)
    startKoin {
      androidContext(this@GoTripMap)
      modules(appModule)
    }
  }
}
