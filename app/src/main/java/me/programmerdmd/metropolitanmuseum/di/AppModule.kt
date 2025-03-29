package me.programmerdmd.metropolitanmuseum.di

import android.app.Application
import androidx.room.Room
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepositoryImpl
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepository
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepositoryImpl
import me.programmerdmd.metropolitanmuseum.objects.database.AppDatabase
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteDao
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailScreenViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchViewModel
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
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
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