package ru.mclient.ui.record.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R
import java.time.LocalDate
import java.time.LocalTime

data class RecordProfilePageState(
    val record: Record?,
    val isStatusAvailable: Boolean,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {
    data class Record(
        val client: Client,
        val staff: Staff,
        val services: List<Service>,
        val cost: Long,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val date: LocalDate,
        val status: RecordStatus,
    )

    class Client(
        val name: String,
        val formattedPhone: String,
    )

    class Staff(
        val name: String,
        val role: String,
        val codename: String,
    )

    class Service(
        val title: String,
        val cost: Long,
        val formattedCost: String,
    )

    enum class RecordStatus {
        NOT_COME,
        COME,
        WAITING,
    }
}

fun RecordProfilePageState.copyWithStatus(status: RecordProfilePageState.RecordStatus): RecordProfilePageState {
    return copy(record = record?.copy(status = status))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfilePage(
    state: RecordProfilePageState,
    onCome: () -> Unit,
    onNotCome: () -> Unit,
    onWaiting: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    statusComeContent: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            AnimatedVisibility(
                visible = state.isStatusAvailable && !scrollState.isScrollInProgress,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .outlined()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Итого: ${(state.record?.cost ?: 0).toMoney()}",
                            modifier = Modifier.align(Alignment.End)
                        )
                        RecordProfileClientStatusComponent(
                            state = state,
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
                        )
                        if (state.record?.status == RecordProfilePageState.RecordStatus.COME)
                            Box {
                                statusComeContent()
                            }
                    }
                }
            }
        }
    ) {
        DesignedRefreshColumn(
            refreshing = state.isRefreshing,
            onRefresh = { onRefresh() },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            if (state.record != null) {
                RecordProfileDateComponent(
                    record = state.record,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                RecordProfileClientComponent(
                    record = state.record,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                RecordProfileStaffComponent(
                    record = state.record,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                RecordProfileServicesComponent(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (state.isStatusAvailable)
                    Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileDateComponent(
    record: RecordProfilePageState.Record,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Дата и время",
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ListItem(
                headlineText = {
                    Text(record.date.format())
                },
                supportingText = {
                    Text(format(record.startTime, record.endTime))
                },
                trailingContent = {
                    Icon(
                        Icons.Outlined.ArrowForward,
                        contentDescription = "иконка",
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.date),
                        contentDescription = "иконка",
                        modifier = Modifier.size(50.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileClientComponent(
    record: RecordProfilePageState.Record,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Клиент",
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ListItem(
                headlineText = {
                    Text(record.client.name)
                },
                supportingText = {
                    Text(record.client.formattedPhone.toPhoneFormat())
                },
                trailingContent = {
                    Icon(
                        Icons.Outlined.ArrowForward,
                        contentDescription = "иконка",
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.client),
                        contentDescription = "иконка",
                        modifier = Modifier.size(50.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileStaffComponent(
    record: RecordProfilePageState.Record,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Работник",
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ListItem(
                headlineText = {
                    Text(record.staff.name)
                },
                supportingText = {
                    if (record.staff.role.isNotBlank())
                        Text(record.staff.role)
                    else
                        Text(record.staff.codename)
                },
                trailingContent = {
                    Icon(
                        Icons.Outlined.ArrowForward,
                        contentDescription = "иконка",
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.staff),
                        contentDescription = "иконка",
                        modifier = Modifier.size(50.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileServicesComponent(
    state: RecordProfilePageState,
    modifier: Modifier,
) {
    DesignedTitledBlock(
        title = "Услуги",
        modifier = modifier,
    ) {
        if (state.record != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                state.record.services.forEach { service ->
                    ServiceCardComponent(
                        service,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCardComponent(
    service: RecordProfilePageState.Service,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(service.title)
        },
        supportingText = {
            Text(service.formattedCost)
        },
        trailingContent = {
            Icon(
                Icons.Outlined.ArrowForward,
                contentDescription = "иконка",
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.service),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordProfileClientStatusComponent(
    state: RecordProfilePageState,
    onCome: () -> Unit,
    onNotCome: () -> Unit,
    onWaiting: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedFilterChip(
            selected = state.record?.status == RecordProfilePageState.RecordStatus.NOT_COME,
            onClick = { onNotCome() },
            label = { Text(text = "Не пришел") }
        )
        ElevatedFilterChip(
            selected = state.record?.status == RecordProfilePageState.RecordStatus.WAITING,
            onClick = { onWaiting() },
            label = { Text(text = "Ожидание") }
        )
        ElevatedFilterChip(
            selected = state.record?.status == RecordProfilePageState.RecordStatus.COME,
            onClick = { onCome() },
            label = { Text(text = "Пришел") }
        )
    }

}

@Preview
@Composable
fun RecordProfilePagePreview() {
    var state by remember {
        mutableStateOf(
            RecordProfilePageState(
                record = RecordProfilePageState.Record(
                    client = RecordProfilePageState.Client(
                        name = "Игорь Александрович Войтенко",
                        formattedPhone = "79132281337".toPhoneFormat()
                    ),
                    staff = RecordProfilePageState.Staff(
                        name = "Дядя Толик",
                        role = "Парикмахер широкого спектра",
                        codename = "tolik"
                    ),
                    services = listOf(
                        RecordProfilePageState.Service(
                            "Стрижка под ноль",
                            250,
                            "250 ₽",
                        ),
                        RecordProfilePageState.Service(
                            "Покраска ногтей в черный",
                            400,
                            "400 ₽",
                        ),
                        RecordProfilePageState.Service(
                            "Массаж простаты",
                            1500,
                            "1500 ₽",
                        )
                    ),
                    cost = 0,
                    startTime = LocalTime.of(20, 21),
                    endTime = LocalTime.of(20, 50),
                    date = LocalDate.of(2022, 11, 5),
                    status = RecordProfilePageState.RecordStatus.WAITING
                ),
                isRefreshing = false,
                isLoading = false,
                isStatusAvailable = true,
            )
        )
    }
    Surface {
        RecordProfilePage(
            state = state,
            onWaiting = {
                state = state.copyWithStatus(RecordProfilePageState.RecordStatus.WAITING)
            },
            onNotCome = {
                state = state.copyWithStatus(RecordProfilePageState.RecordStatus.NOT_COME)
            },
            onCome = { state = state.copyWithStatus(RecordProfilePageState.RecordStatus.COME) },
            onRefresh = { /*TODO*/ },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            statusComeContent = {}
        )
    }
}