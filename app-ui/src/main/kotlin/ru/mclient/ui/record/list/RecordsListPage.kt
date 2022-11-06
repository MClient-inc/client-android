package ru.mclient.ui.record.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.DesignedListPoint
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R
import java.time.LocalDate
import java.time.LocalTime


class RecordsListPageState(
    val records: List<Record>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val isFailure: Boolean,
) {

    class Record(
        val id: Long,
        val client: Client,
        val staff: Staff,
        val services: List<Service>,
        val cost: Long,
        val formattedCost: String,
        val formattedDate: String,
    )

    class Client(
        val name: String,
        val formattedPhone: String,
    )

    class Staff(
        val name: String,
    )

    class Service(
        val title: String,
        val cost: Long
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordsListPage(
    state: RecordsListPageState,
    onClick: (RecordsListPageState.Record) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier,
) {
    DesignedRefreshColumn(refreshing = state.isFailure, onRefresh = onRefresh) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 100.dp),
            modifier = modifier,
            content = {
                items(state.records, key = RecordsListPageState.Record::id) { record ->
                    RecordItem(
                        record = record,
                        onClick = onClick,
                        modifier = Modifier
                            .width(180.dp)
                            .outlined(),
                    )
                }
            })
    }
}


@Composable
private fun RecordItem(
    record: RecordsListPageState.Record,
    onClick: (RecordsListPageState.Record) -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onClick(record) })
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(id = R.drawable.user),
            contentDescription = null,
            modifier = Modifier.size(75.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f, fill = true)
        ) {
            DesignedListPoint(
                Icons.Outlined.Menu,
                record.formattedDate,
                style = MaterialTheme.typography.labelLarge
            )
            DesignedListPoint(
                Icons.Outlined.Menu,
                record.client.name,
            )
            DesignedListPoint(
                Icons.Outlined.Menu,
                record.client.formattedPhone,
            )
            if (record.cost > 0)
                DesignedListPoint(
                    Icons.Outlined.Menu,
                    record.formattedCost,
                )
            DesignedListPoint(
                icon = Icons.Outlined.Menu,
                text = record.staff.name,
            )
        }
        IconButton(onClick = { onClick(record) }) {
            Icon(Icons.Outlined.ArrowForward, contentDescription = null)
        }

    }
}

@Composable
@Preview
fun RecordsListPagePreview() {
    RecordsListPage(
        state = RecordsListPageState(
            records = List(100) {
                RecordsListPageState.Record(
                    id = it.toLong(),
                    client = RecordsListPageState.Client(
                        name = "Владик",
                        formattedPhone = "78000000000".toPhoneFormat()
                    ),
                    staff = RecordsListPageState.Staff("Володя"),
                    services = listOf(
                        RecordsListPageState.Service(
                            title = "Стрижка",
                            cost = 500
                        ),
                        RecordsListPageState.Service(
                            title = "Стрижка",
                            cost = 500
                        ),
                    ),
                    formattedDate = LocalDate.now().format() + " " + format(
                        LocalTime.now(),
                        LocalTime.now()
                    ),
                    cost = 100 * it.toLong(),
                    formattedCost = "${100 * it.toLong()} ₽",
                )
            },
            isLoading = false,
            isFailure = false,
            isRefreshing = false,
        ),
        onClick = {},
        onRefresh = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    )
}