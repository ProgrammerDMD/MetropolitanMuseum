package me.programmerdmd.metropolitanmuseum

import android.app.Application
import me.programmerdmd.metropolitanmuseum.di.appModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }

}