package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R
import java.time.LocalTime

class RecordCreateTimeBlockState(
    val time: LocalTime?,
    val isAvailable: Boolean,
)

@Composable
fun RecordCreateTimeBlock(
    state: RecordCreateTimeBlockState,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Время",
        modifier = modifier,
    ) {
        RecordCreateItem(
            text = state.time?.format() ?: "не указано",
            isAvailable = state.isAvailable,
            onClick = onClick,
            icon = painterResource(id = R.drawable.time),
            modifier = modifier,
        )
    }
}