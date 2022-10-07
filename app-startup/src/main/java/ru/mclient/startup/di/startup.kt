package ru.mclient.startup.di

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.KoinApplication

@Suppress("unused") // in Manifest
class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication {
        return context.initializeKoin()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

}