package ru.mclient.ui.client.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.formatAsRussianNumber
import ru.mclient.ui.utils.PhoneNumberVisualTransformation
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField

data class ClientCreatePageState(
    val name: String,
    val phone: String,
    val isLoading: Boolean,
    val error: String?,
)

data class ClientCreatePageInput(
    val name: String,
    val phone: String,
)

fun ClientCreatePageState.toInput(
    name: String = this.name,
    phone: String = this.phone,
): ClientCreatePageInput {
    return ClientCreatePageInput(
        name = name,
        phone = phone
    )
}

@Composable
fun ClientCreatePage(
    modifier: Modifier,
    state: ClientCreatePageState,
    onUpdate: (ClientCreatePageInput) -> Unit,
    onCreate: () -> Unit,
    onAgreement: () -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DesignedTextField(
            value = state.name,
            onValueChange = {
                onUpdate(state.toInput(name = it))
            },
            label = "Имя, псевдоним",
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedTextField(
            value = state.phone,
            onValueChange = {
                onUpdate(state.toInput(phone = it))
            },
            label = "Номер телефона",
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PhoneNumberVisualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        DesignedButton(
            text = "Добавить клиента",
            onClick = { onCreate() }
        )
        Text(
            buildAnnotatedString {
                append("Продолжая, вы подтвержаете, что клиент дал согласие на ")
                withStyle(//our own style
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    append(
                        "обработку персональных данных"
                    )
                }
            },
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAgreement,
                )
        )
    }
}

@Preview
@Composable
fun ServiceCreatePreview(
) {
    var state by remember {
        mutableStateOf(
            ClientCreatePageState(
                name = "",
                phone = "",
                isLoading = false,
                error = null
            )
        )
    }

    ClientCreatePage(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUpdate = {
            state = state.copy(
                name = it.name,
                phone = it.phone.formatAsRussianNumber()
            )
        },
        onCreate = {},
        onAgreement = {},
    )
}