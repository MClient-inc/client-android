package ru.mclient.common.record.create

import java.time.LocalDate

data class RecordCreateDateSelectorState(
    val isAvailable: Boolean,
    val isSuccess: Boolean,
    val date: LocalDate?,
)

interface RecordCreateDateSelector {

    val state: RecordCreateDateSelectorState

    fun onDateSelected(date: LocalDate?)

}