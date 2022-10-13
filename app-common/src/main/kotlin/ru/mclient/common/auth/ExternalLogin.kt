package ru.mclient.common.auth

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

@Parcelize
data class ExternalLoginState(
    val message: String,
    val timerEndAt: Long,
    val isRetryAvailable: Boolean,
    val account: Account? = null,
) : Parcelable {

    @Parcelize
    data class Account(
        val id: Long,
        val username: String,
        val name: String,
        val avatar: String?
    ) : Parcelable

}

interface ExternalLogin {

    val state: StateFlow<ExternalLoginState>

    fun onRetry()
    fun onAuthenticated()

}