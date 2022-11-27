package ru.mclient.ui.agreement

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.agreement.Agreement
import ru.mclient.common.agreement.AgreementState

private fun AgreementState.toUI(): AgreementBlockState {
    return AgreementBlockState(title = title, content = content)
}

@Composable
fun AgreementUI(
    component: Agreement,
    modifier: Modifier,
) {
    AgreementBlock(
        state = component.state.toUI(),
        modifier = modifier,
    )
}
