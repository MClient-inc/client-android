package ru.mclient.ui.record.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.profile.RecordProfile
import ru.mclient.common.record.profile.RecordProfileState

fun RecordProfileState.toUI(): RecordPageState {
    return RecordPageState(
        record = record?.toUI(),
        isRefreshing = isRefreshing,
        isLoading = isLoading,
    )
}

fun RecordProfileState.Record.toUI(): RecordPageState.Record {
    return RecordPageState.Record(
        client = RecordPageState.Client(
            name = client.name,
            formattedPhone = client.phone.toPhoneFormat()
        ),
        staff = RecordPageState.Staff(
            name = staff.name,
            codename = staff.codename,
            role = staff.role
        ),
        services = services.map {
            RecordPageState.Service(
                title = it.title,
                cost = it.cost,
                description = it.description,
                formattedCost = it.cost.toMoney()
            )
        },
        cost = totalCost,
        startTime = time.start,
        endTime = time.end,
        date = schedule.date,
        status = RecordPageState.RecordStatus.WAITING
    )
}

@Composable
fun RecordProfileUI(
    component: RecordProfile,
    modifier: Modifier,
) {
    RecordProfilePage(
        state = component.state.toUI(),
        onCome = {},
        onNotCome = {},
        onWaiting = {},
        onRefresh = {},
        modifier = modifier,
    )
}