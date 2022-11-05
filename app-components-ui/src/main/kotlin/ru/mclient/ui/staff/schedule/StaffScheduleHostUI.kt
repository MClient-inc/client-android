package ru.mclient.ui.staff.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.schedule.StaffScheduleHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffScheduleHostUI(
    component: StaffScheduleHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier
    ) {
        StaffScheduleUI(
            component = component.staffSchedule,
            modifier = Modifier.fillMaxWidth()
        )
    }
}