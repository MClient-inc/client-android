package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R
import java.time.LocalDate

class RecordCreateDateBlockState(
    val date: LocalDate?,
    val isAvailable: Boolean,
)

@Composable
fun RecordCreateDateBlock(
    state: RecordCreateDateBlockState,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Дата",
        modifier = modifier,
    ) {
        RecordCreateItem(
            text = state.date?.format() ?: "не указано",
            isAvailable = state.isAvailable,
            onClick = onClick,
            icon = painterResource(id = R.drawable.date),
        )
    }
}