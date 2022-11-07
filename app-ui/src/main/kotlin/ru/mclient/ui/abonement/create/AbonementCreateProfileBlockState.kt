package ru.mclient.ui.abonement.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.ui.view.DesignedTextField

data class AbonementCreateProfileBlockState(
    val title: String,
)

data class AbonementCreateProfileBlockInput(
    val title: String,
)

fun AbonementCreateProfileBlockState.toInput(
    title: String = this.title,
): AbonementCreateProfileBlockInput {
    return AbonementCreateProfileBlockInput(
        title = title,
    )
}

@Composable
fun AbonementCreateProfileBlock(
    state: AbonementCreateProfileBlockState,
    onUpdate: (AbonementCreateProfileBlockInput) -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedTextField(
        value = state.title,
        onValueChange = { onUpdate(state.toInput(title = it)) },
        label = "Название",
        modifier = modifier,
    )
}