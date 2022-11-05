package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.create.RecordCreateStaffSelector
import ru.mclient.ui.staff.list.StaffListUI
import ru.mclient.ui.view.DesignedDropdownMenu

@Composable
fun RecordCreateStaffSelectorUI(
    component: RecordCreateStaffSelector,
    modifier: Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        RecordCreateItem(
            text = "Испольнитель: ${component.state.selectedStaff?.name ?: "не указано"}",
            isAvailable = component.state.isAvailable,
            onClick = component::onExpand,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedDropdownMenu(
            expanded = component.state.isExpanded,
            onDismissRequest = component::onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        ) {
            StaffListUI(
                component = component.staffList,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}