package ru.mclient.ui.record.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.record.list.RecordsList
import ru.mclient.common.record.list.RecordsListState

fun RecordsListState.toUI(): RecordsListPageState {
    return RecordsListPageState(
        records = records.map { record ->
            RecordsListPageState.Record(
                id = record.id,
                cost = record.cost,
                services = record.services.map { service ->
                    RecordsListPageState.Service(title = service.title)
                },
                client = RecordsListPageState.Client(
                    name = record.client.name,
                    phone = record.client.phone,
                    formattedPhone = record.client.formattedPhone,
                ),
                staff = RecordsListPageState.Staff(record.staff.name),
                formattedDate = record.formattedTime,
                formattedCost = record.formattedCost,
            )
        },
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        isFailure = isFailure
    )
}

@Composable
fun RecordsListUI(
    component: RecordsList,
    modifier: Modifier,
) {
    RecordsListPage(
        state = component.state.toUI(),
        onClick = remember(component) { { component.onSelect(it.id) } },
        onRefresh = component::onRefresh,
        modifier = modifier
    )
}