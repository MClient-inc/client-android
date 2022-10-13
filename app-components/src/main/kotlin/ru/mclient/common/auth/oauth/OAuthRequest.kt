package ru.mclient.common.auth.oauth

import android.content.Intent
import kotlinx.coroutines.flow.SharedFlow

sealed class AuthorizationState {

    data class Success(val code: String, val codeVerifier: String) : AuthorizationState()

    object Failure : AuthorizationState()

}

interface OAuthRequest {

    val state: SharedFlow<AuthorizationState>

    fun onAuthorizationCompleted(data: Intent)

    fun getOpenPageIntent(): Intent

}