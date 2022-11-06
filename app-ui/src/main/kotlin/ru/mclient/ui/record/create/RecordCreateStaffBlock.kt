package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R

data class RecordCreateStaffBlockState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val selectedStaff: Staff?,
) {
    class Staff(
        val id: Long,
        val name: String,
    )
}

@Composable
fun RecordCreateStaffBlock(
    state: RecordCreateStaffBlockState,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    expandedContent: @Composable () -> Unit,
) {
    DesignedOutlinedTitledBlock(
        title = "Исполнитель",
        modifier = modifier,
    ) {
        RecordCreateItem(
            text = state.selectedStaff?.name ?: "не указано",
            isAvailable = state.isAvailable,
            onClick = onExpand,
            icon = painterResource(id = R.drawable.staff),
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