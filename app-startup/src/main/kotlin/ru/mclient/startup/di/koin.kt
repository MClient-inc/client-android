package ru.mclient.startup.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.ksp.generated.module
import ru.mclient.local.LocalModule
import ru.mclient.mvi.mviModule
import ru.mclient.network.ApiNetworkModule
import ru.mclient.startup.httpClientModule
import ru.mclient.startup.oauth.androidOAuthModule

fun Context.initializeKoin(): KoinApplication {
    return org.koin.core.context.startKoin {
        androidContext(this@initializeKoin)
        androidLogger(Level.DEBUG)
        modules(
            androidxActivityModule, httpClientModule,
            androidOAuthModule,
            baseMviModule,
            mviModule,
            ApiNetworkModule().module,
            LocalModule().module
        )
    }
}