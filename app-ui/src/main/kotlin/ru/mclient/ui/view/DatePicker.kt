package ru.mclient.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


@Composable
fun rememberDatePicker(
    initialDate: LocalDate = LocalDate.now(),
    onSelected: (LocalDate?) -> Unit = { },
): DatePicker {
    val context = LocalContext.current
    return remember {
        val dialog = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(initialDate.toMillis())
            .setTitleText("Выбрать дату")
            .build()
        dialog.addOnPositiveButtonClickListener {
            onSelected(dialog.selection?.toLocalDate())
        }
        DatePicker(
            dialog,
            (context as FragmentActivity).supportFragmentManager,
        )
    }
}

class DatePicker(
    private val dialog: MaterialDatePicker<Long>,
    private val fragmentManager: FragmentManager,
) {

    fun show() {
        if (fragmentManager.findFragmentByTag("date_picker") != null)
            return
        dialog.show(fragmentManager, "date_picker")
    }

    fun hide() {
        dialog.dialog?.hide()
    }

}

fun LocalDate.toMillis(): Long {
    return atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
}


fun Long.toLocalDate(): LocalDate {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC).toLocalDate()
}
