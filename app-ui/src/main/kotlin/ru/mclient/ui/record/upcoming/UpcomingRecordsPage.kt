package ru.mclient.ui.record.upcoming

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.view.DesignedDivider
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedListPoint
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
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
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Ближайщие записи", style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.75f, fill = true),
            )
            TextButton(onClick = onMoreDetails) {
                Text("Ещё")
            }
        }
        DesignedDivider(modifier = Modifier.fillMaxWidth())
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(180.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(state.records, key = UpcomingRecordsPageState.Record::id) { record ->
                    RecordItem(
                        record = record,
                        modifier = Modifier
                            .width(180.dp)
                            .outlined()
                            .clickable(onClick = { onClick(record) })
                            .padding(10.dp),
                    )
                }
            })
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
        RecordStaff(staff = record.schedule.staff)
        RecordSchedule(schedule = record.schedule, time = record.time)
        RecordClient(client = record.client)
        RecordServices(services = record.services)
    }
}


@Composable
fun RecordServices(
    services: List<UpcomingRecordsPageState.Service>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        services.forEach { record ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DesignedIcon(
                    Icons.Outlined.Menu.toDesignedDrawable(),
                    modifier = Modifier.size(15.dp)
                )
                DesignedText(
                    record.title.toDesignedString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun RecordClient(
    client: UpcomingRecordsPageState.Client,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DesignedListPoint(
            icon = Icons.Outlined.Person,
            text = client.name,
            style = MaterialTheme.typography.bodyMedium,
        )
        DesignedListPoint(
            icon = Icons.Outlined.Phone,
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
        icon = Icons.Outlined.Person,
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
        Text(
            schedule.date.format(),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            format(time.start, time.end),
            style = MaterialTheme.typography.labelMedium
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
        onClick = {},
        onMoreDetails = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}