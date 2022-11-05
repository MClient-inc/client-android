package ru.mclient.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime


@Composable
fun rememberTimePicker(
    initialTime: LocalTime = LocalTime.now(),
    onSelected: (LocalTime) -> Unit = { },
): TimePicker {
    val context = LocalContext.current
    return remember {
        val dialog = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(initialTime.hour)
            .setMinute(initialTime.minute)
            .setTitleText("Выбрать время")
            .build()
        dialog.addOnPositiveButtonClickListener {
            onSelected(LocalTime.of(dialog.hour, dialog.minute))
        }
        TimePicker(
            dialog,
            (context as FragmentActivity).supportFragmentManager,
        )
    }
}

class TimePicker(
    private val dialog: MaterialTimePicker,
    private val fragmentManager: FragmentManager,
) {

    fun show() {
        if (fragmentManager.findFragmentByTag("time_picker") != null)
            return
        dialog.show(fragmentManager, "time_picker")
    }

    fun hide() {
        dialog.dialog?.hide()
    }

}
