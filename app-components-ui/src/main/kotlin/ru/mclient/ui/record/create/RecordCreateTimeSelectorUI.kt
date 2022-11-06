package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.create.RecordCreateTimeSelector
import ru.mclient.common.record.create.RecordCreateTimeSelectorState
import ru.mclient.ui.view.rememberTimePicker
import java.time.LocalTime

@Composable
fun RecordCreateTimeSelectorUI(
    component: RecordCreateTimeSelector,
    modifier: Modifier,
) {
    val dialog = rememberTimePicker(
        initialTime = component.state.time ?: LocalTime.now(),
        onSelected = component::onTimeSelected,
    )
    RecordCreateTimeBlock(
        state = component.state.toUI(),
        onClick = dialog::show,
        modifier = modifier
    )
}

private fun RecordCreateTimeSelectorState.toUI(): RecordCreateTimeBlockState {
    return RecordCreateTimeBlockState(
        time = time, isAvailable = isAvailable,
    )
}
