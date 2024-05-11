package com.android.gotripmap.di


import android.location.Geocoder
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.db.RoutesAndEntriesDB
import com.android.gotripmap.data.mappers.RouteMapper
import com.android.gotripmap.data.mappers.RoutesAndEntriesMapper
import com.android.gotripmap.data.mappers.SearchEntryMapper
import com.android.gotripmap.data.network.AuthAPIFactory
import com.android.gotripmap.data.network.AuthApiService
import com.android.gotripmap.data.network.EntriesAPIFactory
import com.android.gotripmap.data.network.EntriesAPIService
import com.android.gotripmap.data.network.OTPApiFactory
import com.android.gotripmap.data.network.OTPApiService
import com.android.gotripmap.data.network.RouteUpdateAPIFactory
import com.android.gotripmap.data.network.RouteUpdateApiService
import com.android.gotripmap.data.network.SearchAPIFactory
import com.android.gotripmap.data.network.SearchApiService
import com.android.gotripmap.data.network.UserDataAPIFactory
import com.android.gotripmap.data.network.UserDataAPIService
import com.android.gotripmap.data.repositories.StatusRepositoryImpl
import com.android.gotripmap.data.repositories.EntriesRepositoryImpl
import com.android.gotripmap.data.repositories.PiLocationManagerImpl
import com.android.gotripmap.data.repositories.ProfileRepositoryImpl
import com.android.gotripmap.data.repositories.RoutesRepositoryImpl
import com.android.gotripmap.data.repositories.VoiceToTextParserImpl
import com.android.gotripmap.domain.repositories.StatusRepository
import com.android.gotripmap.domain.repositories.EntriesRepository
import com.android.gotripmap.domain.repositories.PiLocationManager
import com.android.gotripmap.domain.repositories.ProfileRepository
import com.android.gotripmap.domain.repositories.RoutesRepository
import com.android.gotripmap.domain.repositories.VoiceToTextParser
import com.android.gotripmap.domain.usecases.routes.LoadRouteUseCase
import com.android.gotripmap.domain.usecases.routes.ChangeLikedUseCase
import com.android.gotripmap.domain.usecases.geoposition.GetAddressesUseCase
import com.android.gotripmap.domain.usecases.profile.GetProfileUseCase
import com.android.gotripmap.domain.usecases.profile.StartAuthUseCase
import com.android.gotripmap.domain.usecases.profile.StartOTPUseCase
import com.android.gotripmap.domain.usecases.profile.UpdateProfileUseCase
import com.android.gotripmap.domain.usecases.routes.AddEntryUseCase
import com.android.gotripmap.domain.usecases.routes.AddRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.CreateEntryUseCase
import com.android.gotripmap.domain.usecases.routes.DeleteRecentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.GetCurrentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.GetHistoryUseCase
import com.android.gotripmap.domain.usecases.routes.GetLikedUseCase
import com.android.gotripmap.domain.usecases.routes.MakeEntryCurrentUseCase
import com.android.gotripmap.domain.usecases.routes.UpdateEntryUseCase
import com.android.gotripmap.domain.usecases.status.GetStatusUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.GetSpeechUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StartListeningUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StopListeningUseCase
import com.android.gotripmap.presentation.viewmodels.DialogVM
import com.android.gotripmap.presentation.viewmodels.EditProfileVM
import com.android.gotripmap.presentation.viewmodels.HistoryVM
import com.android.gotripmap.presentation.viewmodels.LikedVM
import com.android.gotripmap.presentation.viewmodels.MainVM
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

  factory {
    CoroutineScope(Dispatchers.IO)
  }

  single<StatusRepository> {
    StatusRepositoryImpl(androidContext())
  }

  single<EntriesAPIService> {
    EntriesAPIFactory.apiService
  }

  single<EntriesRepository> {
    EntriesRepositoryImpl(get(), get(), get(),get(),get(),get(),get())
  }

  single<RouteUpdateApiService> {
    RouteUpdateAPIFactory.apiService
  }

  single<SearchApiService> {
    SearchAPIFactory.apiService
  }

  single<RoutesRepository> {
    RoutesRepositoryImpl(get(), get(), get(),get(),get(),get(),get())
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
    CreateEntryUseCase(get())
  }

  single<AuthApiService> {
    AuthAPIFactory.apiService
  }

  single<OTPApiService> {
    OTPApiFactory.apiService
  }

  single<UserDataAPIService> {
    UserDataAPIFactory.apiService
  }

  single<ProfileRepository> {
    ProfileRepositoryImpl(androidContext(),get(),get(),get(),get(),get())
  }

  single {
    StartAuthUseCase(get())
  }

  single {
    GetProfileUseCase(get())
  }

  single {
    UpdateProfileUseCase(get())
  }
  single {
    AddEntryUseCase(get())
  }

  single {
    StartOTPUseCase(get())
  }

  single {
    AddEntryUseCase(get())
  }

  single {
    AddRoutesUseCase(get())
  }

  single {
    UpdateEntryUseCase(get())
  }

  single {
    GetStatusUseCase(get())
  }

  single { DeleteRecentRoutesUseCase(get()) }

  viewModel { MainVM(get()) }
  viewModel { HistoryVM(get(), get()) }
  viewModel { LikedVM(get(), get()) }
  viewModel { SearchScreenVM(get(), get(), get(), get(),get(),get()) }
  viewModel { DialogVM(get(), get(), get(), get(), get()) }
  viewModel { EditProfileVM(get(), get(), get(),get(),get(),get()) }
}
