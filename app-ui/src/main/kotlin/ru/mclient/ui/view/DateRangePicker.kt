package ru.mclient.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import kotlin.Pair
import androidx.core.util.Pair as AndroidPair


@Composable
fun rememberDateRangePicker(
    initialDate: Pair<LocalDate, LocalDate>? = null,
    onSelected: (Pair<LocalDate, LocalDate>?) -> Unit = { },
): DateRangePicker {
    val context = LocalContext.current
    return remember {
        val dialog = MaterialDatePicker.Builder
            .dateRangePicker()
            .setSelection(initialDate?.toPair())
            .setTitleText("Выбрать промежуток")
            .build()
        dialog.addOnPositiveButtonClickListener {
            onSelected(dialog.selection?.toPair())
        }
        DateRangePicker(
            dialog,
            (context as FragmentActivity).supportFragmentManager,
        )
    }
}

private fun AndroidPair<Long, Long>.toPair(): Pair<LocalDate, LocalDate> {
    return first.toLocalDate() to second.toLocalDate()
}

private fun Pair<LocalDate, LocalDate>.toPair(): AndroidPair<Long, Long> {
    return AndroidPair.create(first.toMillis(), second.toMillis())
}

class DateRangePicker(
    private val dialog: MaterialDatePicker<AndroidPair<Long, Long>>,
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
