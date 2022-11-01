package ru.mclient.ui.record.upcoming

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.upcoming.UpcomingRecords
import ru.mclient.common.record.upcoming.UpcomingRecordsState


private fun UpcomingRecordsState.toUI(): UpcomingRecordsPageState {
    return UpcomingRecordsPageState(
        records = records.map { it.toUI() },
        isLoading = isLoading,
        isFailure = isFailure
    )
}

private fun UpcomingRecordsState.Record.toUI(): UpcomingRecordsPageState.Record {
    return UpcomingRecordsPageState.Record(
        id = id,
        schedule = UpcomingRecordsPageState.Schedule(
            staff = UpcomingRecordsPageState.Staff(
                name = schedule.staff.name
            ),
            date = schedule.date,
        ),
        client = UpcomingRecordsPageState.Client(
            name = client.name,
            phone = client.phone,
        ),
        services = services.map { service ->
            UpcomingRecordsPageState.Service(
                title = service.title,
            )
        },
        time = UpcomingRecordsPageState.TimeOffset(
            start = time.start,
            end = time.end,
        )
    )
}


@Composable
fun RecordsUpcomingUI(component: UpcomingRecords, modifier: Modifier) {
    UpcomingRecordsPage(
        state = component.state.toUI(),
        onClick = { component.onSelect(it.id) },
        modifier = modifier
    )
}