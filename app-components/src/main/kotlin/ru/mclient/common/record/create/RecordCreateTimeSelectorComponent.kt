package ru.mclient.common.record.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateTimeSelectorStore
import java.time.LocalTime

class RecordCreateTimeSelectorComponent(
    componentContext: DIComponentContext,
    private val onSelectedDate: (LocalTime?) -> Unit,
) : RecordCreateTimeSelector, DIComponentContext by componentContext {

    private val store: RecordCreateTimeSelectorStore = getSavedStateStore("record_create_time")

    override val state: RecordCreateTimeSelectorState by store.states(this) { it.toState() }

    private fun RecordCreateTimeSelectorStore.State.toState(): RecordCreateTimeSelectorState {
        return RecordCreateTimeSelectorState(
            isAvailable = isAvailable,
            time = time,
            isSuccess = time != null,
        )
    }

    override fun onTimeSelected(time: LocalTime) {
        onSelectedDate.invoke(time)
        store.accept(RecordCreateTimeSelectorStore.Intent.ChangeTme(time))
    }
}