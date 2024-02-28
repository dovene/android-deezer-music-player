package com.example.kotlinmusic.di

import android.app.Application
import androidx.room.Room
import com.example.kotlinmusic.data.database.AppDatabase
import com.example.kotlinmusic.data.repository.FavoriteTrackRepository
import com.example.kotlinmusic.data.repository.IFavoriteTrackRepository
import com.example.kotlinmusic.network.ApiInterface
import com.example.kotlinmusic.ui.viewmodels.FavoritesViewModel
import com.example.kotlinmusic.ui.viewmodels.MusicSearchViewModel
import com.example.kotlinmusic.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/*
Using Koin for dependency injections
All dependencies for the application are injected here, this is a global DI container
 */
class KotlinMusic : Application() {

    private val appModule = module {
        single<ApiInterface> {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }

        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "kotlinmusic_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        single { get<AppDatabase>().favoriteTrackDao() }
        single<IFavoriteTrackRepository> { FavoriteTrackRepository(get()) }

        viewModel { MusicSearchViewModel(apiInterface = get(), favoriteTrackRepository = get()) }
        viewModel { FavoritesViewModel(favoriteTrackRepository = get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KotlinMusic)
            modules(appModule)
        }
    }
}

