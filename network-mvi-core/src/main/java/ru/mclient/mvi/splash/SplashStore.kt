package ru.mclient.mvi.splash

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface SplashStore : Store<SplashStore.Intent, SplashStore.State, SplashStore.Label> {

    sealed class Intent

    sealed class State: Parcelable {

        @Parcelize
        object Authenticated: State()

        @Parcelize
        object Unauthenticated: State()

        @Parcelize
        object Loading: State()

    }

    sealed class Label
}