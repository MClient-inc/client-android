package ru.mclient.common.record.create

import ru.mclient.common.bar.TopBarHost

class RecordCreateHostState(
    val isButtonAvailable: Boolean,
    val isLoading: Boolean,
    val totalCost: Long,
)

interface RecordCreateHost : TopBarHost {

    val state: RecordCreateHostState

    val clientsSelector: RecordCreateClientSelector

    val staffSelector: RecordCreateStaffSelector

    val dateSelector: RecordCreateDateSelector

    val timeSelector: RecordCreateTimeSelector

    val servicesSelector: RecordCreateServicesSelector

    fun onContinue()

}