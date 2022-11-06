package ru.mclient.ui.record.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.profile.RecordProfileHost
import ru.mclient.common.record.profile.RecordProfileState
import ru.mclient.ui.bar.TopBarHostUI

fun RecordProfileState.Record.toUI(): RecordPageState {
    return RecordPageState(
        record = RecordPageState.Record(
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
        ),
        isRefreshing = false,
        isLoading = false
    )
}

@Composable
fun RecordProfileUI(
    component: RecordProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier
    ) {
        component.recordProfile.state.record?.let {
            RecordProfilePage(
                state = it.toUI(),
                onCome = {},
                onNotCome = {},
                onWaiting = {},
                onRefresh = {},
                modifier = modifier,
            )
        }
    }
}