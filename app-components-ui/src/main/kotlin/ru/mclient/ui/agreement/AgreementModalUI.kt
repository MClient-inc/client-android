package ru.mclient.ui.agreement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.agreement.AgreementModal
import ru.mclient.ui.ModalUI

@Composable
fun AgreementModalUI(
    component: AgreementModal,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    ModalUI(
        component = component,
        sheetContent = {
            AgreementUI(component = component.agreement, modifier = Modifier.fillMaxWidth())
        },
        content = content,
        modifier = modifier,
    )
}