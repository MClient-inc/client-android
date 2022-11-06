package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.create.RecordCreateStaffSelector
import ru.mclient.common.record.create.RecordCreateStaffSelectorState
import ru.mclient.ui.staff.list.StaffListUI

@Composable
fun RecordCreateStaffSelectorUI(
    component: RecordCreateStaffSelector,
    modifier: Modifier,
) {
    RecordCreateStaffBlock(
        state = component.state.toUI(),
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        modifier = Modifier.fillMaxWidth(),
    ) {
        StaffListUI(component = component.staffList, modifier = Modifier.fillMaxWidth())
    }
}

private fun RecordCreateStaffSelectorState.toUI(): RecordCreateStaffBlockState {
    return RecordCreateStaffBlockState(
        isExpanded = isExpanded,
        isAvailable = isAvailable,
        selectedStaff = selectedStaff?.let {
            RecordCreateStaffBlockState.Staff(
                it.id,
                it.name
            )
        }
    )
}
