package ru.mclient.ui.record.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.profile.RecordProfile
import ru.mclient.common.record.profile.RecordProfileState
import ru.mclient.ui.record.create.ClientAbonementsSelectorUI

fun RecordProfileState.toUI(): RecordProfilePageState {
    return RecordProfilePageState(
        record = record?.toUI(),
        isRefreshing = isRefreshing,
        isLoading = isLoading,
        isStatusAvailable = record != null
    )
}

fun RecordProfileState.Record.toUI(): RecordProfilePageState.Record {
    return RecordProfilePageState.Record(
        client = RecordProfilePageState.Client(
            name = client.name,
            formattedPhone = client.phone.toPhoneFormat()
        ),
        staff = RecordProfilePageState.Staff(
            name = staff.name,
            codename = staff.codename,
            role = staff.role
        ),
        services = services.map {
            RecordProfilePageState.Service(
                title = it.title,
                cost = it.cost,
                formattedCost = it.cost.toMoney()
            )
        },
        cost = totalCost,
        startTime = time.start,
        endTime = time.end,
        date = schedule.date,
        status = when (status) {
            RecordProfileState.RecordStatus.NOT_COME -> RecordProfilePageState.RecordStatus.NOT_COME
            RecordProfileState.RecordStatus.COME -> RecordProfilePageState.RecordStatus.COME
            RecordProfileState.RecordStatus.WAITING -> RecordProfilePageState.RecordStatus.WAITING
        }
    )
}

@Composable
fun RecordProfileUI(
    component: RecordProfile,
    modifier: Modifier,
) {
    RecordProfilePage(
        state = component.state.toUI(),
        onCome = component::onCome,
        onNotCome = component::onNotCome,
        onWaiting = component::onWaiting,
        onRefresh = component::onRefresh,
        modifier = modifier,
    ) {
        Column {
            ClientAbonementsSelectorUI(
                component = component.abonements,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedButton(
                onClick = component::onApplyAbonements,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Применить")
            }
        }
    }
}