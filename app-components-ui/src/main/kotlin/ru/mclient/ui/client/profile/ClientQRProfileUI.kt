package ru.mclient.ui.client.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.profile.ClientQRProfile
import ru.mclient.common.client.profile.ClientQRProfileState
import ru.mclient.ui.ModalUI

@Composable
fun ClientQRProfileUI(
    component: ClientQRProfile,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalUI(
        component = component,
        sheetContent = {
            ClientQRCodeBlock(
                state = component.state.toUI(),
                onShare = component::onShare,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = modifier,
        content = content
    )
}

private fun ClientQRProfileState.toUI(): ClientQRCodeBlockState {
    return ClientQRCodeBlockState(
        code = code?.code,
        isLoading = isLoading,
        isShareAvailable = isShareAvailable,
    )
}
