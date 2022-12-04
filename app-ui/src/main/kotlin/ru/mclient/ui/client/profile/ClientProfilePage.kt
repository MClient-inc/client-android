package ru.mclient.ui.client.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.abonement.clientcreate.NoAbonement
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedListPoint
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTitledBlock
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
        val company: Company,
        val time: Time,
        val staff: Staff,
        val services: List<Service>,
        val totalCost: Long
    )

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
}

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
        if (state.records != null)
            ClientRecords(
                state = state,
                onRecord = { onRecord(it) },
                modifier = Modifier.fillMaxWidth(),
            )
        if (state.abonements != null)
            ClientProfileAbonementsList(
                abonements = state.abonements,
                onCreateAbonement = onCreateAbonement,
                modifier = Modifier.fillMaxWidth()
            )

        if (state.networkAnalytics != null)
            ClientNetworkAnalyticsItem(
                networkAnalytics = state.networkAnalytics,
                modifier = Modifier.fillMaxWidth()
            )
        if (state.companyAnalytics != null)
            ClientCompanyAnalyticsItem(
                companyAnalytics = state.companyAnalytics,
                modifier = Modifier.fillMaxWidth()
            )
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
    DesignedTitledBlock(
        title = "Абонементы",
        button = "Добавить",
        onClick = onCreateAbonement,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (abonements.isEmpty())
                NoAbonement(
                    modifier = Modifier
                        .fillMaxWidth()
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
fun ClientNetworkAnalyticsItem(
    networkAnalytics: ClientProfilePageState.NetworkAnalytics,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(title = "Сеть", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            NetworkAnalyticsItemBlock(
                item = networkAnalytics.analytics
            )
        }
    }
}

@Composable
fun ClientCompanyAnalyticsItem(
    companyAnalytics: ClientProfilePageState.CompanyAnalytics,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(title = "Компания", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CompanyAnalyticsItemBlock(
                item = companyAnalytics.analytics
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkAnalyticsItemBlock(
    item: ClientProfilePageState.ClientAnalyticsItem,
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
        headlineText = {
            Text(text = "${item.comeCount}", color = Color.Green)
        },
        supportingText = {},
        overlineText = {
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
        headlineText = {
            Text(text = "${item.notComeCount}", color = Color.Red)
        },
        supportingText = {},
        overlineText = {
            Text(text = "Не пришёл")
        }
    )


    ListItem(
        leadingContent = {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.sizeIn(0.dp),
                tint = Color(255, 150, 0, 255)
            )
        },
        headlineText = {
            Text(text = "${item.waitingCount}", color = Color(255, 150, 0, 255))
        },
        supportingText = {},
        overlineText = {
            Text(text = "В ожидании")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyAnalyticsItemBlock(
    item: ClientProfilePageState.ClientAnalyticsItem,
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
        headlineText = {
            Text(text = "${item.comeCount}", color = Color.Green)
        },
        supportingText = {},
        overlineText = {
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
        headlineText = {
            Text(text = "${item.notComeCount}", color = Color.Red)
        },
        supportingText = {},
        overlineText = {
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
        headlineText = {
            Text(text = "${item.waitingCount}", color = Color(255, 150, 0, 255))
        },
        supportingText = {},
        overlineText = {
            Text(text = "В ожидании")
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientRecords(
    state: ClientProfilePageState,
    onRecord: (Long) -> Unit,
    modifier: Modifier
) {
    DesignedOutlinedTitledBlock(title = "Ближайшие записи пользователя", modifier = modifier) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(180.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
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
                                        .padding(5.dp)
                                        .width(180.dp)
                                        .height(70.dp)
                                        .outlined()
                                        .clickable {
                                            onRecord(record.id)
                                        }
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
        modifier = modifier.padding(3.dp)
    ) {
        RecordSchedule(schedule = record.time)
        RecordStaff(staff = record.staff)
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
private fun RecordSchedule(
    schedule: ClientProfilePageState.Time,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = painterResource(id = R.drawable.date),
            text = schedule.date.format(),
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
        )
        DesignedListPoint(
            icon = painterResource(id = R.drawable.time),
            text = format(schedule.start, schedule.end),
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
        )
    }
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
                    totalCost = 1000,
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
                    totalCost = 500,
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