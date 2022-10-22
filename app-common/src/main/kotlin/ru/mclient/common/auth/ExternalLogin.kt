package ru.mclient.common.auth


data class ExternalLoginState(
    val message: String,
    val timerEndAt: Long,
    val isRetryAvailable: Boolean,
    val account: Account? = null,
) {

    data class Account(
        val id: Long,
        val username: String,
        val name: String,
        val avatar: String?
    )

}

interface ExternalLogin {

    val state: ExternalLoginState

    fun onRetry()
    fun onAuthenticated()

}