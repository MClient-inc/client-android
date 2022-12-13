package ru.mclient.ui.client.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.mclient.ui.abonement.clientcreate.NoAbonement
import ru.mclient.ui.client.profile.ClientProfilePageState.AnalyticsType.*
import ru.mclient.ui.record.profile.RecordProfilePageState.RecordStatus.*
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.record.upcoming.EmptyRecords
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedDropdownMenuItem
import ru.mclient.ui.view.DesignedListPoint
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTab
import ru.mclient.ui.view.DesignedTabRow
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R
import java.time.LocalDate
import java.time.LocalTime

data class ClientProfilePageState(
    val profile: Profile?,
    val abonements: List<ClientAbonement>?,
    val networkAnalytics: NetworkAnalytics?,
    val companyAnalytics: CompanyAnalytics?,
    val records: List<Record>?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Profile(
        val name: String,
        val phone: String,
    )

    class ClientAbonement(
        val id: Long,
        val usages: Int,
        val abonement: Abonement,
    )

    class Abonement(
        val title: String,
        val subabonement: Subabonement,
    )

    class Subabonement(
        val title: String,
        val maxUsages: Int,
    )

    // Analytics
    class NetworkAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    class CompanyAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    class ClientAnalyticsItem(
        var notComeCount: Long,
        var comeCount: Long,
        var waitingCount: Long,
        val totalCount: Long,
    )

    class Record(
        val id: Long,
        val status: RecordStatus,
        val company: Company,
        val time: Time,
        val staff: Staff,
        val services: List<Service>,
        val totalCost: Long,
    )

    enum class RecordStatus {
        WAITING, COME, NOT_COME,
    }

    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    class Staff(
        val id: Long,
        val name: String,
    )

    class Time(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )

    class Company(
        val id: Long,
        val title: String,
    )

    enum class AnalyticsType {
        COMPANY, NETWORK
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ClientProfilePage(
    state: ClientProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onCreateAbonement: () -> Unit,
    onQRCode: () -> Unit,
    onRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.profile != null)
            ClientProfileHeader(
                profile = state.profile,
                onEdit = onEdit,
                onQRCode = onQRCode,
                modifier = Modifier
                    .fillMaxWidth()
            )
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        val type = remember { mutableStateOf(COMPANY) }
        DesignedTabRow {
            val currentPage by remember { derivedStateOf { pagerState.currentPage } }
            DesignedTab(
                selected = currentPage == 0,
                onSelect = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                title = {
                    Text("Записи")
                }
            )
            DesignedTab(
                selected = currentPage == 1,
                onSelect = {
                    if (currentPage == 1) {
                        onCreateAbonement()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                },
                title = {
                    Text("Абонементы")
                },
                trailingContent = {
                    AnimatedVisibility(
                        visible = currentPage == 1,
                        enter = fadeIn(tween(100)) + slideInHorizontally(tween(100)),
                        exit = fadeOut(tween(100)) + slideOutHorizontally(tween(100))
                    ) {
                        Icon(Icons.Outlined.Add, null)
                    }
                }
            )
            Box {
                val isTypeSelecting = rememberSaveable { mutableStateOf(false) }
                DesignedTab(
                    selected = currentPage == 2,
                    onSelect = {
                        if (currentPage == 2) {
                            isTypeSelecting.value = true
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    },
                    title = {
                        if (currentPage != 2) {
                            Text("Аналитика")
                        } else {
                            Text(
                                "Аналитика: ${
                                    when (type.value) {
                                        COMPANY -> "компания"
                                        NETWORK -> "сеть"
                                    }
                                }",
                            )
                        }
                    },
                    trailingContent = {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = currentPage == 2,
                            enter = fadeIn(tween(100)) + slideInHorizontally(tween(100)),
                            exit = fadeOut(tween(100)) + slideOutHorizontally(tween(100))
                        ) {
                            Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                )
                DesignedDropdownMenu(
                    expanded = isTypeSelecting.value,
                    onDismissRequest = { isTypeSelecting.value = false },
                    modifier = Modifier.width(250.dp)
                ) {
                    DesignedDropdownMenuItem(
                        text = {
                            androidx.compose.material.Text("Компания")
                        }, trailingIcon = {
                            AnimatedVisibility(type.value == COMPANY) {
                                androidx.compose.material.Icon(Icons.Outlined.Check, "выбрано")
                            }
                        },
                        onClick = {
                            type.value = COMPANY
                            isTypeSelecting.value = false
                        }
                    )
                    DesignedDropdownMenuItem(
                        text = {
                            androidx.compose.material.Text("Сеть")
                        }, trailingIcon = {
                            AnimatedVisibility(type.value == NETWORK) {
                                androidx.compose.material.Icon(Icons.Outlined.Check, "выбрано")
                            }
                        },
                        onClick = {
                            type.value = NETWORK
                            isTypeSelecting.value = false
                        }
                    )
                }
            }
        }
        HorizontalPager(count = 3, state = pagerState, verticalAlignment = Alignment.Top) { page ->
            when (page) {
                0 -> {
                    if (state.records != null)
                        ClientRecords(
                            state = state,
                            onRecord = { onRecord(it) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                }

                1 -> {
                    if (state.abonements != null)
                        ClientProfileAbonementsList(
                            abonements = state.abonements,
                            onCreateAbonement = onCreateAbonement,
                            modifier = Modifier.fillMaxWidth()
                        )
                }

                2 -> {
                    if (state.networkAnalytics != null)
                        ClientAnalyticsItem(
                            selectedType = type.value,
                            network = state.networkAnalytics,
                            company = state.companyAnalytics,
                            modifier = Modifier.fillMaxWidth()
                        )
                }
            }
        }


    }
}

@Composable
fun ClientProfileHeader(
    profile: ClientProfilePageState.Profile,
    onEdit: () -> Unit,
    onQRCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .outlined()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.client),
            modifier = Modifier.size(75.dp),
            contentDescription = "иконка"
        )
        Column {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.headlineSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                    )
                    Text(
                        text = profile.phone.toPhoneFormat(),
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
                IconButton(onClick = onQRCode) {
                    Icon(
                        painterResource(id = R.drawable.qr_code), contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Редактировать", overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun ClientProfileAbonementsList(
    abonements: List<ClientProfilePageState.ClientAbonement>,
    onCreateAbonement: () -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        if (abonements.isEmpty())
            NoAbonement(
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .clickable(onClick = onCreateAbonement)
            )
        else
            abonements.forEach {
                ClientProfileAbonementItem(
                    abonement = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileAbonementItem(
    abonement: ClientProfilePageState.ClientAbonement,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painterResource(id = R.drawable.subabonements),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        headlineText = {
            Text(
                abonement.abonement.title,
            )
        },
        supportingText = {
            Text(abonement.abonement.subabonement.title)
        },
        trailingContent = {
            Text(
                text = "${abonement.usages}/${abonement.abonement.subabonement.maxUsages}",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier.outlined(),
    )
}

@Composable
fun ClientAnalyticsItem(
    selectedType: ClientProfilePageState.AnalyticsType,
    network: ClientProfilePageState.NetworkAnalytics,
    company: ClientProfilePageState.CompanyAnalytics?,
    modifier: Modifier,
) {
    AnalyticsItemBlock(
        item = when (selectedType) {
            COMPANY -> company?.analytics ?: network.analytics
            NETWORK -> network.analytics
        },
        modifier = modifier.outlined()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyticsItemBlock(
    item: ClientProfilePageState.ClientAnalyticsItem,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp),
                    tint = Color.Green
                )
            },
            supportingText = {
                Text(text = "${item.comeCount}", color = Color.Green)
            },
            headlineText = {
                Text(text = "Пришёл")
            }
        )

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp),
                    tint = Color.Red
                )
            },
            supportingText = {
                Text(text = "${item.notComeCount}", color = Color.Red)
            },
            headlineText = {
                Text(text = "Не пришёл")
            }
        )

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp),
                    tint = Color(255, 150, 0, 255)
                )
            },
            supportingText = {
                Text(text = "${item.waitingCount}", color = Color(255, 150, 0, 255))
            },
            headlineText = {
                Text(text = "В ожидании")
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientRecords(
    state: ClientProfilePageState,
    onRecord: (Long) -> Unit,
    modifier: Modifier,
) {
    if (state.records?.isEmpty() == true) {
        EmptyRecords(modifier = Modifier.fillMaxWidth())
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(180.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.heightIn(max = 600.dp),
            content = {
                if (state.records != null)
                    when {
                        state.isLoading -> {
                            items(state.records.size) {
                                RecordItemPlaceholder(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .outlined()
                                        .padding(10.dp)
                                )
                            }
                        }

                        else -> {
                            items(
                                state.records,
                                key = ClientProfilePageState.Record::id
                            ) { record ->
                                RecordItem(
                                    record = record,
                                    modifier = Modifier
                                        .width(180.dp)
                                        .outlined()
                                        .clickable {
                                            onRecord(record.id)
                                        }
                                        .padding(10.dp)
                                )
                            }
                        }
                    }

            })
    }
}

@Composable
private fun RecordItem(
    record: ClientProfilePageState.Record,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        RecordStatus(status = record.status)
        RecordSchedule(schedule = record.time)
        RecordStaff(staff = record.staff)
        RecordCost(cost = record.totalCost)
    }
}

@Composable
private fun RecordItemPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        RecordClientPlaceholder()
        RecordStaffPlaceholder()
        RecordClientPlaceholder()
    }
}


@Composable
private fun RecordStaff(
    staff: ClientProfilePageState.Staff,
    modifier: Modifier = Modifier,
) {
    DesignedListPoint(
        icon = painterResource(id = R.drawable.staff),
        text = staff.name,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
private fun RecordStatus(
    status: ClientProfilePageState.RecordStatus,
    modifier: Modifier = Modifier,
) {
    when (status) {
        ClientProfilePageState.RecordStatus.WAITING ->
            CompositionLocalProvider(LocalContentColor provides Color(255, 150, 0, 255)) {
                DesignedListPoint(
                    Icons.Outlined.PlayArrow,
                    text = "Ожидание",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier,
                )
            }

        ClientProfilePageState.RecordStatus.COME -> CompositionLocalProvider(LocalContentColor provides Color.Green) {
            DesignedListPoint(
                Icons.Outlined.KeyboardArrowRight,
                text = "Пришел",
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier,
            )
        }

        ClientProfilePageState.RecordStatus.NOT_COME ->
            CompositionLocalProvider(LocalContentColor provides Color.Red) {
                DesignedListPoint(
                    Icons.Outlined.KeyboardArrowLeft,
                    text = "Не пришел",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier,
                )
            }
    }

}


@Composable
private fun RecordSchedule(
    schedule: ClientProfilePageState.Time,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = painterResource(id = R.drawable.date),
            text = schedule.date.format(),
            style = MaterialTheme.typography.bodyMedium,
        )
        DesignedListPoint(
            icon = painterResource(id = R.drawable.time),
            text = format(schedule.start, schedule.end),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun RecordCost(
    cost: Long,
    modifier: Modifier = Modifier,
) {
    DesignedListPoint(
        icon = painterResource(id = R.drawable.cost),
        text = "$cost ₽",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
private fun RecordClientPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = painterResource(id = R.drawable.client),
            text = "Имя клиента",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.defaultPlaceholder(),
        )
        DesignedListPoint(
            icon = painterResource(id = R.drawable.phone),
            text = "Номер телефона",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.defaultPlaceholder(),
        )
    }
}

@Composable
private fun RecordStaffPlaceholder(
    modifier: Modifier = Modifier,
) {
    DesignedListPoint(
        icon = painterResource(id = R.drawable.staff),
        text = "Имя работника",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.defaultPlaceholder(),
    )
}

@Composable
private fun RecordSchedulePlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = painterResource(id = R.drawable.date),
            text = "Дата записи",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.defaultPlaceholder(),
        )
        DesignedListPoint(
            icon = painterResource(id = R.drawable.time),
            text = "Время",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.defaultPlaceholder(),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ClientProfilePagePreview() {
    ClientProfilePage(
        state = ClientProfilePageState(
            profile = ClientProfilePageState.Profile(
                name = "Александр Сергеевич Пушкин",
                phone = "78005553535"
            ),
            abonements = listOf(),
            companyAnalytics = ClientProfilePageState.CompanyAnalytics(
                id = 1,
                title = "Test-Company",
                analytics = ClientProfilePageState.ClientAnalyticsItem(
                    comeCount = 2,
                    notComeCount = 1,
                    waitingCount = 1,
                    totalCount = 4
                )
            ),
            networkAnalytics = ClientProfilePageState.NetworkAnalytics(
                id = 1,
                title = "Test-Network",
                analytics = ClientProfilePageState.ClientAnalyticsItem(
                    comeCount = 43,
                    notComeCount = 12,
                    waitingCount = 1,
                    totalCount = 66
                )
            ),
            records = listOf(
                ClientProfilePageState.Record(
                    id = 1,
                    company = ClientProfilePageState.Company(
                        id = 1,
                        title = "Company-001",
                    ),
                    services = listOf(
                        ClientProfilePageState.Service(
                            id = 1,
                            cost = 1000,
                            title = "Массаж"
                        )
                    ),
                    staff = ClientProfilePageState.Staff(
                        id = 1,
                        name = "Василий Зиновий Игнатьич"
                    ),
                    time = ClientProfilePageState.Time(
                        date = LocalDate.of(2022, 12, 15),
                        start = LocalTime.of(22, 30),
                        end = LocalTime.of(23, 30)
                    ),
                    totalCost = 1000, status = ClientProfilePageState.RecordStatus.COME,

                    ),
                ClientProfilePageState.Record(
                    id = 2,
                    company = ClientProfilePageState.Company(
                        id = 1,
                        title = "Company-001",
                    ),
                    services = listOf(
                        ClientProfilePageState.Service(
                            id = 2,
                            cost = 50,
                            title = "Парикмахер"
                        )
                    ),
                    staff = ClientProfilePageState.Staff(
                        id = 1,
                        name = "Константин Шура Павлович"
                    ),
                    time = ClientProfilePageState.Time(
                        date = LocalDate.of(2022, 12, 15),
                        start = LocalTime.of(21, 45),
                        end = LocalTime.of(22, 15)
                    ),
                    totalCost = 500, status = ClientProfilePageState.RecordStatus.COME,

                    ),
                ClientProfilePageState.Record(
                    id = 3,
                    company = ClientProfilePageState.Company(
                        id = 1,
                        title = "Company-001",
                    ),
                    services = listOf(
                        ClientProfilePageState.Service(
                            id = 3,
                            cost = 50,
                            title = "Мастер маникюра"
                        )
                    ),
                    staff = ClientProfilePageState.Staff(
                        id = 3,
                        name = "Анастасия Головченко Станиславовна"
                    ),
                    time = ClientProfilePageState.Time(
                        date = LocalDate.of(2022, 12, 16),
                        start = LocalTime.of(14, 30),
                        end = LocalTime.of(16, 10)
                    ),
                    totalCost = 1500,
                    status = ClientProfilePageState.RecordStatus.COME,
                )
            ),
            isLoading = false,
            isRefreshing = false
        ),
        onRefresh = {},
        onEdit = {},
        onQRCode = {},
        onCreateAbonement = {},
        onRecord = {},
        modifier = Modifier
            .fillMaxSize()
    )
}