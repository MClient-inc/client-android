package ru.mclient.common.record.create

import java.time.LocalTime

data class RecordCreateTimeSelectorState(
    val isAvailable: Boolean,
    val isSuccess: Boolean,
    val time: LocalTime?,
)

interface RecordCreateTimeSelector {

    val state: RecordCreateTimeSelectorState

    fun onTimeSelected(time: LocalTime)

}