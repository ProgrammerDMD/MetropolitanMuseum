package me.programmerdmd.metropolitanmuseum.di

import android.app.Application
import androidx.room.Room
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepositoryImpl
import me.programmerdmd.metropolitanmuseum.objects.database.AppDatabase
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteDao
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailScreenViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.favorites.FavoritesScreenViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeScreenViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.trending.TrendingScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        provideDatabase(androidApplication())
    }

    single {
        provideFavoriteDao(get())
    }

    viewModelOf(::SearchViewModel)
    viewModelOf(::DetailScreenViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::FavoritesScreenViewModel)
    viewModelOf(::TrendingScreenViewModel)
    singleOf(::MuseumRepositoryImpl) bind MuseumRepository::class
}

fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "metropolitan-museum-db"
    ).build()
}

fun provideFavoriteDao(database: AppDatabase): FavoriteDao {
    return database.favoriteDao()
}