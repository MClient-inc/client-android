package ru.mclient.startup.di

import androidx.appcompat.app.AppCompatActivity
import org.koin.dsl.module

class AndroidxActivityModule {

    val module = module {
        scope<AppCompatActivity> {
            scoped { get<AppCompatActivity>().activityResultRegistry }
        }
    }

}
