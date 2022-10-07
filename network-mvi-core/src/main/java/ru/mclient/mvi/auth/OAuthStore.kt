package ru.mclient.mvi.auth

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface OAuthLoginStore :
    Store<OAuthLoginStore.Intent, OAuthLoginStore.State, OAuthLoginStore.Label> {


    sealed class State : Parcelable {

        @Parcelize
        object Empty : State()

        @Parcelize
        data class OAuthPage(val timerEndAt: Long) : State()

        @Parcelize
        object TokensLoading : State()

        @Parcelize
        object Failure : State()

        @Parcelize
        class Success(
            val username: String,
            val name: String,
            val avatar: String?,
        ) : State()

    }

    sealed class Intent {

        object StartOAuthPage : Intent()

        data class OAuthPageCompleted(val code: String, val codeVerifier: String) : Intent()

        object OAuthPageFailure : Intent()

    }

    sealed class Label {

        object OpenOAuthPage : Label()

    }

}