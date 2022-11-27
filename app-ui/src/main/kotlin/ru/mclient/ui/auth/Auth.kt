package ru.mclient.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.shafran.ui.R


data class AuthUIState(
    val isLoginAvailable: Boolean,
    val isRegisterAvailable: Boolean,
)

@Composable
fun Auth(
    state: AuthUIState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAgreement: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painterResource(id = R.drawable.icon),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(250.dp)
                    .fillMaxSize()
            )
        }
        DesignedButton(
            text = "Уже есть аккаунт",
            onClick = onLogin,
            enabled = state.isLoginAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedButton(
            text = "Я новичок",
            onClick = onRegister,
            enabled = state.isRegisterAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            buildAnnotatedString {
                append("Продолжая, вы принимаете ")
                withStyle(//our own style
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    append(
                        "пользовательское соглашения"
                    )
                }
            },
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = onAgreement,
                )
                .padding(5.dp)
        )
    }
}