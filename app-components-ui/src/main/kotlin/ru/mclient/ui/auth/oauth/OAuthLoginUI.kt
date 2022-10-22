package ru.mclient.ui.auth.oauth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.auth.ExternalLogin
import ru.mclient.common.auth.ExternalLoginState
import ru.mclient.ui.auth.ExternalLoginPage
import ru.mclient.ui.auth.ExternalLoginPageState

fun ExternalLoginState.toUI(): ExternalLoginPageState {
    return ExternalLoginPageState(
        message = message,
        timerEndAt = timerEndAt,
        isTimerStarted = timerEndAt != 0L,
        isRetryAvailable = isRetryAvailable,
        account = account?.let {
            ExternalLoginPageState.Account(
                name = it.name,
                username = it.username,
                avatar = it.avatar
            )
        }
    )
}

@Composable
fun ExternalLoginUI(
    component: ExternalLogin,
    modifier: Modifier
) {
    ExternalLoginPage(
        state = component.state.toUI(),
        onRetry = component::onRetry,
        onAuthenticated = component::onAuthenticated,
        modifier = modifier
    )
}