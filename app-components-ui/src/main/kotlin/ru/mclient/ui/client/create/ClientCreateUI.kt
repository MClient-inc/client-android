package ru.mclient.ui.client.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.client.create.ClientCreate
import ru.mclient.common.client.create.ClientCreateState

@Composable
fun ClientCreateUI(
    component: ClientCreate,
    modifier: Modifier,
) {
    ClientCreatePage(
        modifier = modifier,
        state = component.state.toUI(),
        onUpdate = remember(component) { { component.onUpdate(it.name, it.phone) } },
        onCreate = component::onCreate,
    )
}

private fun ClientCreateState.toUI(): ClientCreatePageState {
    return ClientCreatePageState(
        name = name,
        phone = phone,
        isLoading = isLoading,
        error = error
    )
}