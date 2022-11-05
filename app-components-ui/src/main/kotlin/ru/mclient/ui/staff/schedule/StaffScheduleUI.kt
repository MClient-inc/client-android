package ru.mclient.ui.staff.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.schedule.StaffSchedule
import ru.mclient.common.staff.schedule.StaffScheduleState
import ru.mclient.ui.view.rememberDatePicker
import ru.mclient.ui.view.rememberDateRangePicker


@Composable
fun StaffScheduleUI(
    component: StaffSchedule,
    modifier: Modifier,
) {
    val datePicker = rememberDatePicker(onSelected = { date ->
        if (date != null) {
            component.onSelect(date)
        }
    })
    val dateRangePicker =
        rememberDateRangePicker(onSelected = { range ->
            if (range != null) {
                component.onSelect(range.first, range.second)
            }
        })
    StaffSchedulePage(
        state = component.state.toUI(),
        onSingle = datePicker::show,
        onRange = dateRangePicker::show,
        onContinue = component::onContinue,
        modifier = modifier,
    )
}

fun StaffScheduleState.toUI(): StaffSchedulePageState {
    return StaffSchedulePageState(
        selectedSchedule = selectedSchedule.map {
            StaffSchedulePageState.Schedule(
                start = it.start,
                end = it.end,
            )
        },
        isAvailable = isAvailable
    )
}