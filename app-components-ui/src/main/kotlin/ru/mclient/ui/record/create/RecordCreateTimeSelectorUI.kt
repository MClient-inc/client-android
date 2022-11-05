package ru.mclient.ui.record.create

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.mclient.common.record.create.RecordCreateTimeSelector
import ru.mclient.ui.record.upcoming.format
import java.time.LocalTime

@Composable
fun RecordCreateTimeSelectorUI(
    component: RecordCreateTimeSelector,
    modifier: Modifier,
) {
    val state = rememberMaterialDialogState()
    RecordCreateItem(
        text = "Время: ${component.state.time?.format() ?: "не указано"}",
        isAvailable = component.state.isAvailable,
        onClick = state::show,
        modifier = modifier,
    )
    MaterialDialog(
        dialogState = state,
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        buttons = {
            positiveButton("Применить")
            negativeButton("Отменить")
        }
    ) {
        timepicker(
            initialTime = component.state.time ?: LocalTime.now(),
            title = "Выбрать время",
            is24HourClock = true,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary.copy(0.3f),
                inactiveBackgroundColor = MaterialTheme.colorScheme.onBackground.copy(0.3f),
                activeTextColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTextColor = MaterialTheme.colorScheme.onBackground,
                inactivePeriodBackground = Color.Transparent,
                selectorColor = MaterialTheme.colorScheme.primary,
                selectorTextColor = MaterialTheme.colorScheme.onPrimary,
                headerTextColor = MaterialTheme.colorScheme.onBackground,
                borderColor = MaterialTheme.colorScheme.onBackground
            ),
            onTimeChange = component::onTimeSelected,
        )
    }
}