package ru.mclient.ui.record.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.list.RecordsListPageState
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R
import java.time.LocalDate
import java.time.LocalTime

data class RecordPageState(
    val record: Record?,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) {
    data class Record(
        val client: RecordsListPageState.Client,
        val staff: RecordsListPageState.Staff,
        val services: List<RecordsListPageState.Service>,
        val cost: Long,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val date: LocalDate,
        val clientStatus: RecordPageClientStatus
    )
}

data class ClientStatusState(
    val isCome: Boolean,
    val isWaiting: Boolean,
    val isNotCome: Boolean
)

data class ClientStatusInput(
    val isCome: Boolean,
    val isWaiting: Boolean,
    val isNotCome: Boolean
)

fun ClientStatusState.toInput(
    isCome: Boolean = this.isCome,
    isWaiting: Boolean = this.isWaiting,
    isNotCome: Boolean = this.isNotCome
): ClientStatusInput {
    return ClientStatusInput(
        isCome = isCome,
        isWaiting = isWaiting,
        isNotCome = isNotCome
    )
}

@Composable
fun RecordProfilePage(
    state: RecordPageState,
    clientStatusState: ClientStatusState,
    onCome: () -> Unit,
    onNotCome: () -> Unit,
    onWaiting: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (state.record != null) {
            RecordProfileDateComponent(
                record = state.record,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
            RecordProfileClientComponent(
                record = state.record,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )

            RecordProfileStaffComponent(
                record = state.record,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )

            RecordProfileServicesComponent(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )

            RecordProfileClientStatusComponent(
                clientStatusState = clientStatusState,
                onCome = {
                    onCome()
                },
                onNotCome = {
                    onNotCome()
                },
                onWaiting = {
                    onWaiting()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
        }
    }
}


@Composable
fun RecordProfileDateComponent(
    record: RecordPageState.Record,
    modifier: Modifier
) {
//    val formatter =
//        Formatter(startTime = record.startTime, endTime = record.endTime, comeDate = record.date)
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.date),
            contentDescription = "иконка",
            modifier = Modifier.size(65.dp)
        )
        Column {
            Text(
                text = "Дата и время записи",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = formatComeDateAndTime(
                    startTime = record.startTime,
                    endTime = record.endTime,
                    comeDate = record.date
                ),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun RecordProfileClientComponent(
    record: RecordPageState.Record,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.client),
            contentDescription = "иконка",
            modifier = Modifier.size(65.dp)
        )
        Column {
            Text(
                text = "Клиент",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = record.client.name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = record.client.formattedPhone,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecordProfileStaffComponent(
    record: RecordPageState.Record,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.staff),
            contentDescription = "иконка",
            modifier = Modifier.size(65.dp)
        )
        Column {
            Text(
                text = "Работник",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = record.staff.name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecordProfileServicesComponent(
    state: RecordPageState,
    modifier: Modifier
) {
    if (state.record != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            state.record.services.forEach { service ->
                ServiceCardComponent(
                    title = service.title,
                    description = service.description,
                    formattedCost = service.cost.toMoney(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceCardComponent(
    title: String,
    formattedCost: String,
    description: String,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.service),
            contentDescription = "иконка",
            modifier = Modifier.size(65.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formattedCost,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileClientStatusComponent(
    clientStatusState: ClientStatusState,
    onCome: () -> Unit,
    onNotCome: () -> Unit,
    onWaiting: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedFilterChip(
            selected = clientStatusState.isCome,
            onClick = { onCome() },
            label = { Text(text = "Пришел(а)") }
        )
        ElevatedFilterChip(
            selected = clientStatusState.isWaiting,
            onClick = { onWaiting() },
            label = { Text(text = "Ожидание") }
        )
        ElevatedFilterChip(
            selected = clientStatusState.isNotCome,
            onClick = { onNotCome() },
            label = { Text(text = "Не пришел(а)") }
        )
    }

}

@Preview
@Composable
fun RecordProfilePagePreview() {

    var state by remember {
        mutableStateOf(
            ClientStatusState(
                isCome = false,
                isWaiting = true,
                isNotCome = false
            )
        )
    }

    RecordProfilePage(
        state = RecordPageState(
            record = RecordPageState.Record(
                client = RecordsListPageState.Client(
                    name = "Игорь Александрович Войтенко",
                    formattedPhone = "79132281337".toPhoneFormat()
                ),
                staff = RecordsListPageState.Staff(
                    name = "Парикмахер Дядя Толик"
                ),
                services = listOf(
                    RecordsListPageState.Service(
                        "Стрижка под ноль",
                        "",
                        250
                    ),
                    RecordsListPageState.Service(
                        "Покраска ногтей в черный",
                        "",
                        400
                    ),
                    RecordsListPageState.Service(
                        "Массаж простаты",
                        "",
                        1500
                    )
                ),
                cost = 0,
                startTime = LocalTime.of(20, 21),
                endTime = LocalTime.of(20, 50),
                date = LocalDate.of(2022, 11, 5),
                clientStatus = RecordPageClientStatus.WAITING
            ),
            isRefreshing = false,
            isLoading = false
        ),
        clientStatusState = state,
        onWaiting = { state = state.copy(isWaiting = true, isCome = false, isNotCome = false) },
        onNotCome = { state = state.copy(isWaiting = false, isCome = false, isNotCome = true) },
        onCome = { state = state.copy(isWaiting = false, isCome = true, isNotCome = false) },
        onRefresh = { /*TODO*/ }
    )
}