package ru.mclient.startup.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.ksp.generated.module
import ru.mclient.local.LocalModule
import ru.mclient.mvi.NetworkMVIModule
import ru.mclient.network.ApiNetworkModule
import ru.mclient.network.GraphQLNetworkModule
import ru.mclient.startup.GraphQLClientModule
import ru.mclient.startup.HttpClientModule
import ru.mclient.startup.oauth.AndroidOAuthModule

fun Context.initializeKoin(): KoinApplication {
    return org.koin.core.context.startKoin {
        androidContext(this@initializeKoin)
        androidLogger(Level.DEBUG)
        modules(
            AndroidxActivityModule().module,
            AndroidOAuthModule().module,
            NetworkMVIModule().module,
            BaseMviModule().module,
            HttpClientModule().module,
            ApiNetworkModule().module,
            LocalModule().module,
            GraphQLNetworkModule().module,
            GraphQLClientModule().module,
        )
    }
}