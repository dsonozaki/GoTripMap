package com.android.gotripmap.di


import android.location.Geocoder
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.db.RoutesAndEntriesDB
import com.android.gotripmap.data.mappers.RouteMapper
import com.android.gotripmap.data.mappers.RoutesAndEntriesMapper
import com.android.gotripmap.data.mappers.SearchEntryMapper
import com.android.gotripmap.data.network.APIFactory
import com.android.gotripmap.data.network.ApiService
import com.android.gotripmap.data.repositories.EntriesRepositoryImpl
import com.android.gotripmap.data.repositories.PiLocationManagerImpl
import com.android.gotripmap.data.repositories.ProfileRepositoryImpl
import com.android.gotripmap.data.repositories.RoutesRepositoryImpl
import com.android.gotripmap.data.repositories.VoiceToTextParserImpl
import com.android.gotripmap.domain.repositories.EntriesRepository
import com.android.gotripmap.domain.repositories.PiLocationManager
import com.android.gotripmap.domain.repositories.ProfileRepository
import com.android.gotripmap.domain.repositories.RoutesRepository
import com.android.gotripmap.domain.repositories.VoiceToTextParser
import com.android.gotripmap.domain.usecases.routes.LoadRouteUseCase
import com.android.gotripmap.domain.usecases.routes.ChangeLikedUseCase
import com.android.gotripmap.domain.usecases.geoposition.GetAddressesUseCase
import com.android.gotripmap.domain.usecases.profile.GetProfileUseCase
import com.android.gotripmap.domain.usecases.profile.UpdateProfileUseCase
import com.android.gotripmap.domain.usecases.routes.AddEntryUseCase
import com.android.gotripmap.domain.usecases.routes.DeleteRecentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.GetCurrentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.GetHistoryUseCase
import com.android.gotripmap.domain.usecases.routes.GetLikedUseCase
import com.android.gotripmap.domain.usecases.routes.MakeEntryCurrentUseCase
import com.android.gotripmap.domain.usecases.routes.UpdateEntryUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.GetSpeechUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StartListeningUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StopListeningUseCase
import com.android.gotripmap.presentation.viewmodels.DialogVM
import com.android.gotripmap.presentation.viewmodels.EditProfileVM
import com.android.gotripmap.presentation.viewmodels.HistoryVM
import com.android.gotripmap.presentation.viewmodels.LikedVM
import com.android.gotripmap.presentation.viewmodels.SearchScreenVM
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Инъекция зависимостей
 */
val appModule = module {
  single<MainDAO> {
    RoutesAndEntriesDB.create(androidContext()).mainDao()
  }
  single {
    SearchEntryMapper()
  }

  single {
    Gson()
  }

  single<RouteMapper> {
    RouteMapper(get())
  }

  single {
    RoutesAndEntriesMapper(get(), get())
  }

  single<EntriesRepository> {
    EntriesRepositoryImpl(get(), get(), get())
  }
  factory {
    CoroutineScope(Dispatchers.IO)
  }
  single<RoutesRepository> {
    RoutesRepositoryImpl(get(), get(), get(),get())
  }
  single {
    GetHistoryUseCase(get())
  }
  single {
    GetLikedUseCase(get())
  }
  single {
    ChangeLikedUseCase(get())
  }
  single {
    LoadRouteUseCase(get())
  }
  single {
    MakeEntryCurrentUseCase(get())
  }

  single {
    GetCurrentRoutesUseCase(get())
  }

  single {
    Geocoder(androidContext())
  }

  single<PiLocationManager> {
    PiLocationManagerImpl(androidContext(), get())
  }


  single {
    GetAddressesUseCase(get())
  }

  single<VoiceToTextParser> {
    VoiceToTextParserImpl(androidContext(),get())
  }

  single {
    GetSpeechUseCase(get())
  }

  single {
    StartListeningUseCase(get())
  }

  single {
    StopListeningUseCase(get())
  }

  single {
    AddEntryUseCase(get())
  }

  single<ProfileRepository> {
    ProfileRepositoryImpl(androidContext())
  }

  single {
    GetProfileUseCase(get())
  }

  single {
    UpdateProfileUseCase(get())
  }

  single<ApiService> {
    APIFactory.apiService
  }

  single {
    UpdateEntryUseCase(get())
  }

  single { DeleteRecentRoutesUseCase(get()) }

  viewModel { HistoryVM(get(), get()) }
  viewModel { LikedVM(get(), get()) }
  viewModel { SearchScreenVM(get(), get(), get(), get(),get(),get()) }
  viewModel { DialogVM(get(), get(), get(), get(), get()) }
  viewModel { EditProfileVM(get(), get()) }
}
