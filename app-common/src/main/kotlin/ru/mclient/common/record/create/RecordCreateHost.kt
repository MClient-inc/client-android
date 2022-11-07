package ru.mclient.common.record.create

import ru.mclient.common.bar.TopBarHost
import ru.mclient.common.service.list.ServicesListSelector

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

    val servicesSelector: ServicesListSelector

    fun onContinue()

}