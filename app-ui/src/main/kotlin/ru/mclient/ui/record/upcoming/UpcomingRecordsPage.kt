package ru.mclient.ui.record.upcoming

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedListPoint
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.Month.*
import java.time.format.DateTimeFormatter

class UpcomingRecordsPageState(
    val records: List<Record>,
    val isLoading: Boolean,
    val isFailure: Boolean,
) {

    class Record(
        val id: Long,
        val client: Client,
        val time: TimeOffset,
        val schedule: Schedule,
        val services: List<Service>,
    )

    class Schedule(
        val staff: Staff,
        val date: LocalDate,
    )


    class TimeOffset(
        val start: LocalTime,
        val end: LocalTime,
    )

    class Client(
        val name: String,
        val phone: String,
    )

    class Staff(
        val name: String,
    )

    class Service(
        val title: String,
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpcomingRecordsPage(
    state: UpcomingRecordsPageState,
    onClick: (UpcomingRecordsPageState.Record) -> Unit,
    onMoreDetails: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier,
) {
    DesignedTitledBlock(
        title = "Ближайщие записи",
        button = "Ещё",
        onClick = onMoreDetails,
        modifier = modifier
    ) {
        val count = LocalConfiguration.current.screenWidthDp / 180
        if (!state.isLoading && state.records.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .outlined()
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painterResource(id = R.drawable.empty),
                    contentDescription = null,
                    modifier = Modifier.size(75.dp)
                )
                Text("Пусто", style = MaterialTheme.typography.headlineSmall)
                DesignedButton(text = "Обновить", onClick = onRefresh)
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(180.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
                content = {
                    when {
                        state.isLoading -> {
                            items(count) {
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
                                key = UpcomingRecordsPageState.Record::id
                            ) { record ->
                                RecordItem(
                                    record = record,
                                    modifier = Modifier
                                        .width(180.dp)
                                        .outlined()
                                        .clickable(onClick = { onClick(record) })
                                        .padding(10.dp),
                                )
                            }
                        }
                    }

                })
        }

    }
}

@Composable
private fun RecordItem(
    record: UpcomingRecordsPageState.Record,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        RecordSchedule(schedule = record.schedule, time = record.time)
        RecordStaff(staff = record.schedule.staff)
        RecordClient(client = record.client)
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
private fun RecordClient(
    client: UpcomingRecordsPageState.Client,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = painterResource(id = R.drawable.client),
            text = client.name,
            style = MaterialTheme.typography.bodyMedium,
        )
        DesignedListPoint(
            icon = painterResource(id = R.drawable.phone),
            text = client.phone.toPhoneFormat(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun RecordStaff(
    staff: UpcomingRecordsPageState.Staff,
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
    schedule: UpcomingRecordsPageState.Schedule,
    time: UpcomingRecordsPageState.TimeOffset,
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
            text = format(time.start, time.end),
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

private val formatter = DateTimeFormatter.ofPattern("HH:mm")

fun format(from: LocalTime, to: LocalTime): String {
    if (from == to) {
        return "c ${from.format()}"
    }
    return "c ${from.format()} до ${to.format()}"
}

fun LocalTime.format(): String {
    return formatter.format(this)
}

fun format(date1: LocalDate, date2: LocalDate, capitalize: Boolean = true): String {
    if (date1 == date2) {
        return date1.format(capitalize)
    }
    return "С ${date1.format(capitalize)} до ${date2.format(capitalize)}"
}


fun LocalDate.format(capitalize: Boolean = true): String {
    val today = LocalDate.now()
    return when {
        this == today -> if (capitalize) "Сегодня" else "сегодня"
        this == today.plusDays(1) -> if (capitalize) "Завтра" else "завтра"
        this == today.minusDays(1) -> if (capitalize) "Вчера" else "вчера"
        this.year == today.year && this > today -> "${this.dayOfMonth} ${this.month.formatMonth()}"
        else -> "${this.dayOfMonth} ${this.month.formatMonth()} ${this.year}"
    }
}

private fun Month.formatMonth(): String {
    return when (this) {
        JANUARY -> "янв."
        FEBRUARY -> "фев."
        MARCH -> "мар."
        APRIL -> "апр."
        MAY -> "мая"
        JUNE -> "июн."
        JULY -> "июл."
        AUGUST -> "авг."
        SEPTEMBER -> "сен."
        OCTOBER -> "окт."
        NOVEMBER -> "ноя."
        DECEMBER -> "дек."
    }
}

@Composable
@Preview
fun UpcomingRecordsPagePreview() {
    UpcomingRecordsPage(
        state = UpcomingRecordsPageState(
            records = listOf(
                UpcomingRecordsPageState.Record(
                    id = 0,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now(),
                    ),
                    services = listOf(
                        UpcomingRecordsPageState.Service(
                            title = "Стрижка"
                        ),
                        UpcomingRecordsPageState.Service(
                            title = "Стрижка"
                        ),
                    ),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now(),
                    )
                ),
                UpcomingRecordsPageState.Record(
                    id = 3,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now(),
                    ),
                    services = listOf(
                        UpcomingRecordsPageState.Service(
                            title = "Стрижка"
                        ),
                    ),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now().plusHours(2),
                    )
                ),
                UpcomingRecordsPageState.Record(
                    id = 2,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now().plusDays(1),
                    ),
                    services = emptyList(),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now(),
                    )
                ),
                UpcomingRecordsPageState.Record(
                    id = 1,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now().plusDays(5),
                    ),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now().plusHours(2),
                    ),
                    services = emptyList(),
                ),
                UpcomingRecordsPageState.Record(
                    id = 5,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now().plusDays(5),
                    ),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now().plusHours(2),
                    ),
                    services = emptyList(),
                ),
                UpcomingRecordsPageState.Record(
                    id = 6,
                    client = UpcomingRecordsPageState.Client(
                        name = "Владик",
                        phone = "78000000000",
                    ),
                    schedule = UpcomingRecordsPageState.Schedule(
                        staff = UpcomingRecordsPageState.Staff("Володя"),
                        date = LocalDate.now().plusDays(5),
                    ),
                    time = UpcomingRecordsPageState.TimeOffset(
                        start = LocalTime.now(),
                        end = LocalTime.now().plusHours(2),
                    ),
                    services = emptyList(),
                ),
            ),
            isLoading = true,
            isFailure = false,
        ),
        onRefresh = {},
        onClick = {},
        onMoreDetails = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}