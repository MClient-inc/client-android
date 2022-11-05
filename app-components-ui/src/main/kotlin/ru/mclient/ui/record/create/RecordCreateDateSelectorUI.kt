package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.create.RecordCreateDateSelector
import ru.mclient.ui.record.upcoming.format
import ru.mclient.ui.view.rememberDatePicker
import java.time.LocalDate


@Composable
fun RecordCreateDateSelectorUI(
    component: RecordCreateDateSelector,
    modifier: Modifier,
) {
    val dialog = rememberDatePicker(
        initialDate = component.state.date ?: LocalDate.now(),
        onSelected = component::onDateSelected
    )
    RecordCreateItem(
        text = "Дата: ${component.state.date?.format() ?: "не указано"}",
        isAvailable = component.state.isAvailable,
        onClick = dialog::show,
        modifier = modifier,
    )
//    MaterialDialog(
//        dialogState = state,
//        backgroundColor = MaterialTheme.colorScheme.surface,
//        shape = MaterialTheme.shapes.medium,
//        buttons = {
//            positiveButton("Применить", textStyle = MaterialTheme.typography.labelLarge)
//            negativeButton("Отменить", textStyle = MaterialTheme.typography.labelLarge)
//        }
//    ) {
//        datepicker(
//            initialDate = component.state.date ?: LocalDate.now(),
//            title = "Выбрать дату",
//            colors = DatePickerDefaults.colors(
//                headerBackgroundColor = MaterialTheme.colorScheme.primary,
//                headerTextColor = MaterialTheme.colorScheme.onPrimary,
//                calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
//                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
//                dateInactiveBackgroundColor = Color.Transparent,
//                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
//                dateInactiveTextColor = MaterialTheme.colorScheme.onBackground
//            ),
//            onDateChange = component::onDateSelected,
//        )
//    }
}
