package me.programmerdmd.metropolitanmuseum.di

import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepository
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepositoryImpl
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::SearchViewModel)
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
}