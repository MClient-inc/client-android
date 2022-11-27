package ru.mclient.ui.client.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.client.create.ClientCreate
import ru.mclient.common.client.create.ClientCreateState
import ru.mclient.ui.agreement.AgreementModalUI

@Composable
fun ClientCreateUI(
    component: ClientCreate,
    modifier: Modifier,
) {
    AgreementModalUI(component = component.agreement, modifier = modifier) {
        ClientCreatePage(
            modifier = Modifier.fillMaxSize(),
            state = component.state.toUI(),
            onUpdate = remember(component) { { component.onUpdate(it.name, it.phone) } },
            onCreate = component::onCreate,
            onAgreement = component::onAgreement,
        )
    }
}

private fun ClientCreateState.toUI(): ClientCreatePageState {
    return ClientCreatePageState(
        name = name,
        phone = phone,
        isLoading = isLoading,
        error = error
    )
}