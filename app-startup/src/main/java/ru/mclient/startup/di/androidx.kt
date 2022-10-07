package ru.mclient.startup.di

import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.scope.ScopeActivity
import org.koin.dsl.module

val androidxActivityModule = module {
    scope<AppCompatActivity> {
        scoped { get<AppCompatActivity>().activityResultRegistry }
    }
}