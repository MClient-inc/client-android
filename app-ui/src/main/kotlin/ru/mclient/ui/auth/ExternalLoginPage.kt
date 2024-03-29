package ru.mclient.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.currentTime
import ru.mclient.ui.utils.pulse
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

data class ExternalLoginPageState(
    val message: String,
    val timerEndAt: Long,
    val isTimerStarted: Boolean,
    val isRetryAvailable: Boolean,
    val account: Account?,
) {
    data class Account(
        val name: String,
        val username: String,
        val avatar: String?
    )
}

@Composable
fun ExternalLoginPage(
    state: ExternalLoginPageState,
    onRetry: () -> Unit,
    onAuthenticated: () -> Unit,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        modifier = modifier.animateContentSize(),
    ) {
        Image(
            painterResource(id = R.drawable.icon),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .pulse()
        )
        Text(
            text = state.account?.let { "С возвращением, ${it.name}" } ?: state.message,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        AnimatedVisibility(visible = state.account != null) {
            DesignedButton(text = "Продолжить".toDesignedString(), onClick = onAuthenticated)
        }
        AnimatedVisibility(
            visible = state.isRetryAvailable,
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val time = currentTime(threshold = 1000)
                DesignedButton(
                    text = "Перейти в браузер".toDesignedString(),
                    onClick = onRetry,
                    enabled = state.timerEndAt < time,
                )
                AnimatedVisibility(state.timerEndAt >= time) {
                    val seconds = toSeconds(state.timerEndAt, time)
                    Text(
                        text = "(через $seconds сек.)",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

fun toSeconds(timerEndAt: Long, currentTime: Long): Long {
    val seconds = timerEndAt.milliseconds.minus(currentTime.milliseconds)
    return max(0L, seconds.toLong(DurationUnit.SECONDS))
}


