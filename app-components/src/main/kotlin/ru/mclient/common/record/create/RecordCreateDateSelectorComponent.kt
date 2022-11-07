package ru.mclient.common.record.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateDateSelectorStore
import java.time.LocalDate

class RecordCreateDateSelectorComponent(
    componentContext: DIComponentContext,
    private val onSelectedDate: (LocalDate?) -> Unit,
) : RecordCreateDateSelector, DIComponentContext by componentContext {

    private val store: RecordCreateDateSelectorStore = getSavedStateStore("record_create_date")

    override val state: RecordCreateDateSelectorState by store.states(this) { it.toState() }

    private fun RecordCreateDateSelectorStore.State.toState(): RecordCreateDateSelectorState {
        return RecordCreateDateSelectorState(
            isAvailable = isAvailable,
            date = date,
            isSuccess = date != null,
        )
    }

    override fun onDateSelected(date: LocalDate?) {
        onSelectedDate.invoke(date)
        store.accept(RecordCreateDateSelectorStore.Intent.ChangeDate(date))
    }

}