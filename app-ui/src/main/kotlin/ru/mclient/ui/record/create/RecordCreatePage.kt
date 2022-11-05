package ru.mclient.ui.record.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.mclient.ui.record.upcoming.format
import java.time.LocalDate
import java.time.LocalTime


data class RecordCreatePageState(
    val client: Client?,
    val staff: Staff?,
    val date: LocalDate?,
    val time: LocalTime?,
) {

    class Client(
        val id: Long,
        val name: String,
    )

    class Staff(
        val id: Long,
        val name: String,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCreatePage(
    state: RecordCreatePageState,
    onTimeSelected: (LocalTime) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        RecordCreateItem(
            text = "Работник: ${state.staff?.name ?: "не указано"}",
            onClick = {},
            isAvailable = true,
            modifier = Modifier.fillMaxWidth(),
        )
        RecordCreateItem(
            text = "Клиент: ${state.client?.name ?: "не указано"}",
            onClick = {},
            isAvailable = true,
            modifier = Modifier.fillMaxWidth(),
        )
        RecordDatePicker(
            date = state.date,
            onPick = onDateSelected,
            isAvailable = state.staff != null,
            modifier = Modifier.fillMaxWidth(),
        )
        RecordTimePicker(
            time = state.time,
            onPick = onTimeSelected,
            isAvailable = state.staff != null,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun RecordTimePicker(
    time: LocalTime?,
    isAvailable: Boolean,
    onPick: (LocalTime) -> Unit,
    modifier: Modifier,
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Выбрать")
            negativeButton("Отменить")
        }
    ) {
        timepicker(
            initialTime = time ?: LocalTime.now(),
            title = "Время записи",
            timeRange = LocalTime.of(10, 0)..LocalTime.of(20, 0),
            is24HourClock = true,
            onTimeChange = onPick,
        )
    }
    if (isAvailable)
        RecordCreateItem(
            text = "Время: ${time?.format() ?: "не указано"}",
            onClick = { dialogState.show() },
            isAvailable = true,
            modifier = modifier,
        )
    else
        RecordCreateItem(
            text = "Время: сначала выберите работника",
            onClick = { dialogState.show() },
            isAvailable = false,
            modifier = modifier,
        )

}

@Composable
private fun RecordDatePicker(
    date: LocalDate?,
    isAvailable: Boolean,
    onPick: (LocalDate) -> Unit,
    modifier: Modifier,
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Выбрать")
            negativeButton("Отменить")
        }
    ) {
        datepicker(
            initialDate = date ?: LocalDate.now(),
            title = "Дата записи",
            yearRange = 2022..2025,
            onDateChange = onPick,
        )
    }
    if (isAvailable)
        RecordCreateItem(
            text = "Дата: ${date?.format() ?: "не указано"}",
            onClick = { dialogState.show() },
            isAvailable = true,
            modifier = modifier,
        )
    else
        RecordCreateItem(
            text = "Дата: сначала выберите работника",
            onClick = { dialogState.show() },
            isAvailable = false,
            modifier = modifier,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCreateItem(
    text: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(text)
        },
        trailingContent = {
            Icon(Icons.Outlined.ArrowForward, contentDescription = null)
        },
        modifier = modifier.clickable(enabled = isAvailable, onClick = onClick),
    )
}

@Preview
@Composable
fun RecordCreatePagePreview() {
    var state by remember {
        mutableStateOf(
            RecordCreatePageState(
                client = null,
                staff = RecordCreatePageState.Staff(0, "Володя"),
                date = null,
                time = null,
            )
        )
    }
    RecordCreatePage(
        state = state,
        onTimeSelected = { state = state.copy(time = it) },
        onDateSelected = { state = state.copy(date = it) },
        modifier = Modifier.fillMaxSize(),
    )
}