package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.create.RecordCreateClientSelector
import ru.mclient.common.record.create.RecordCreateClientSelectorState
import ru.mclient.ui.client.list.ClientsListUI

@Composable
fun RecordCreateClientsSelectorUI(
    component: RecordCreateClientSelector,
    modifier: Modifier,
) {
    RecordCreateClientBlock(
        state = component.state.toUI(),
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        modifier = modifier,
    ) {
        ClientsListUI(
            component = component.clientsList,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun RecordCreateClientSelectorState.toUI(): RecordCreateClientBlockState {
    return RecordCreateClientBlockState(
        isExpanded = isExpanded,
        isAvailable = isAvailable,
        selectedClient = selectedClient?.let {
            RecordCreateClientBlockState.Client(
                it.id,
                it.name
            )
        }
    )
}
