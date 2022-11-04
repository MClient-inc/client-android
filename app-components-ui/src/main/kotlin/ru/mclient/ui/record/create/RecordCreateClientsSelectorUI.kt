package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.create.RecordCreateClientSelector
import ru.mclient.ui.client.list.ClientsListUI
import ru.mclient.ui.view.DesignedDropdownMenu

@Composable
fun RecordCreateClientsSelectorUI(
    component: RecordCreateClientSelector,
    modifier: Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        RecordCreateItem(
            text = "Клиент: ${component.state.selectedClient?.name ?: "не указано"}",
            isAvailable = component.state.isAvailable,
            onClick = component::onExpand,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedDropdownMenu(
            expanded = component.state.isExpanded,
            onDismissRequest = component::onDismiss,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(350.dp),
        ) {
            ClientsListUI(
                component = component.clientsList,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}