package ru.mclient.ui.staff.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.rememberDatePicker
import ru.mclient.ui.view.rememberDateRangePicker
import ru.shafran.ui.R
import java.time.LocalDate

data class StaffSchedulePageState(
    val selectedSchedule: List<Schedule>,
    val isAvailable: Boolean,
) {
    class Schedule(val start: LocalDate, val end: LocalDate)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StaffSchedulePage(
    state: StaffSchedulePageState,
    onSingle: () -> Unit,
    onRange: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState =
        rememberBottomSheetState(
            BottomSheetValue.Collapsed,
            animationSpec = tween(1000),
            confirmStateChange = BottomSheetValue.Expanded::equals,
        )
    LaunchedEffect(key1 = Unit, block = {
        bottomSheetState.expand()
    })
    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                AnimatedVisibility(state.isAvailable) {
                    StaffNewScheduleItem(
                        onSingle = onSingle,
                        onRange = onRange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedButton(
                    onClick = onContinue,
                    enabled = state.isAvailable,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Принять",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        },
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState
        ),
        sheetElevation = 12.dp,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0)
        )
    ) {
        LazyColumn(
            modifier = modifier.padding(it),
        ) {
            if (state.selectedSchedule.isEmpty()) {
                item {
                    StaffEmptyItem(modifier = Modifier.fillMaxWidth())
                }
            } else {
                items(state.selectedSchedule) { schedule ->
                    StaffScheduleItem(
                        schedule = schedule,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScheduleItem(
    schedule: StaffSchedulePageState.Schedule,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painterResource(id = R.drawable.date),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        },
        headlineText = {
            Text(schedule.toText())
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffEmptyItem(
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                Icons.Outlined.Menu,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        },
        headlineText = {
            Text("Здесь пока пусто")
        },
        modifier = modifier
    )
}

private fun StaffSchedulePageState.Schedule.toText(): String {
    return format(start, end, capitalize = false)
}

@Composable
fun StaffNewScheduleItem(
    onSingle: () -> Unit,
    onRange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        OutlinedButton(
            onClick = onRange, modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Text("Добавить период")
        }
        OutlinedButton(
            onClick = onSingle, modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Text("Добавить день")
        }
    }

}

@Composable
fun StaffSchedulePagePreview() {
    var state by remember {
        mutableStateOf(
            StaffSchedulePageState(
                selectedSchedule = emptyList(),
                isAvailable = true,
            )
        )
    }
    val picker = rememberDatePicker(onSelected = {
        if (it == null)
            return@rememberDatePicker
        state = state.copy(
            selectedSchedule = state.selectedSchedule + StaffSchedulePageState.Schedule(
                start = it,
                end = it
            )
        )
    })
    val rangePicker = rememberDateRangePicker(
        onSelected = {
            if (it == null)
                return@rememberDateRangePicker
            state = state.copy(
                selectedSchedule = state.selectedSchedule + StaffSchedulePageState.Schedule(
                    start = it.first,
                    end = it.second,
                )
            )
        }
    )
    StaffSchedulePage(
        state = state,
        onSingle = picker::show,
        onRange = rangePicker::show,
        onContinue = {},
        modifier = Modifier.fillMaxSize(),
    )
}