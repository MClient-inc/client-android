package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.ui.firey.KalendarFirey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ru.mclient.ui.view.DesignedTitledBlock

class StaffScheduleBlockState(
    val schedule: List<Schedule>?,
) {

    class Schedule(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )

}

@Composable
fun StaffScheduleBlock(
    state: StaffScheduleBlockState,
    onEdit: () -> Unit,
    modifier: Modifier,
) {
    if (state.schedule != null)
        DesignedTitledBlock(
            title = "График работы",
            button = "Изменить",
            onClick = onEdit,
            modifier = modifier
        ) {
            KalendarFirey(
                kalendarEvents = state.schedule.map {
                    KalendarEvent(
                        date = it.date,
                        eventName = "Рабочий день",
                    )
                },
                takeMeToDate = null,
                kalendarDayColors = KalendarDayColors(
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                ),
                kalendarThemeColor = KalendarThemeColor(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    dayBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .offset(y = (-20).dp),
            )
        }
}