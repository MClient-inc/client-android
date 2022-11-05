package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.create.RecordCreateTimeSelector
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.rememberTimePicker
import java.time.LocalTime

@Composable
fun RecordCreateTimeSelectorUI(
    component: RecordCreateTimeSelector,
    modifier: Modifier,
) {
    val state = rememberTimePicker(
        initialTime = component.state.time ?: LocalTime.now(),
        onSelected = component::onTimeSelected,
    )
    RecordCreateItem(
        text = "Время: ${component.state.time?.format() ?: "не указано"}",
        isAvailable = component.state.isAvailable,
        onClick = state::show,
        modifier = modifier,
    )
}