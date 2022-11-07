package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedCreateItem
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R

data class RecordCreateClientBlockState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val selectedClient: Client?,
) {
    class Client(
        val id: Long,
        val name: String,
    )
}

@Composable
fun RecordCreateClientBlock(
    state: RecordCreateClientBlockState,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    expandedContent: @Composable () -> Unit,
) {
    DesignedOutlinedTitledBlock(
        title = "Клиент",
        modifier = modifier,
    ) {
        DesignedCreateItem(
            text = state.selectedClient?.name ?: "не указано",
            isAvailable = state.isAvailable,
            onClick = onExpand,
            icon = painterResource(id = R.drawable.client),
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedDropdownMenu(
            expanded = state.isExpanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        ) {
            expandedContent()
        }
    }
}