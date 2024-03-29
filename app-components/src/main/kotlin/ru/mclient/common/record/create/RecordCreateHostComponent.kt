package ru.mclient.common.record.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.service.list.ServicesListForCompanySelectorComponent
import ru.mclient.common.service.list.ServicesListSelector
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateStore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RecordCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSuccess: () -> Unit,
) : RecordCreateHost, DIComponentContext by componentContext {

    private val store: RecordCreateStore =
        getParameterizedStore { RecordCreateStore.Params(companyId) }

    private fun onSuccess() {
        onSuccess.invoke()
    }

    private val storeState by store.states(this) {
        if (it.isSuccess) {
            onSuccess()
        }
        it
    }

    override val bar: TopBar = ImmutableTopBar(TopBarState("Добавить запись"))

    override val clientsSelector =
        RecordCreateClientSelectorComponent(
            componentContext = childDIContext(key = "record_create_clients"),
            companyId = companyId,
        )


    override val dateSelector: RecordCreateDateSelector =
        RecordCreateDateSelectorComponent(
            componentContext = childDIContext("record_create_date"),
            onSelectedDate = this::onDate
        )
    override val timeSelector: RecordCreateTimeSelector =
        RecordCreateTimeSelectorComponent(
            componentContext = childDIContext("record_create_time"),
            onSelectedDate = this::onTime,
        )
    override val servicesSelector: ServicesListSelector =
        ServicesListForCompanySelectorComponent(
            componentContext = childDIContext(key = "record_create_services"),
            companyId = companyId,
        )

    override val staffSelector =
        RecordCreateStaffSelectorComponent(
            componentContext = childDIContext("record_create_staff"),
            companyId = companyId,
        )

    override val state: RecordCreateHostState
        get() = RecordCreateHostState(
            isButtonAvailable = isAllSelectorsSuccess(),
            isLoading = storeState.isLoading,
            totalCost = servicesSelector.state.selectedServices.sumOf { it.cost }
        )

    override fun onContinue() {
        val staffId = staffSelector.state.selectedStaff?.id ?: return
        val dateTime = getCurrentDateTime() ?: return
        val clientId = clientsSelector.state.selectedClient?.id ?: return
        store.accept(
            RecordCreateStore.Intent.Create(
                clientId = clientId,
                staffId = staffId,
                dateTime = dateTime,
                servicesIds = servicesSelector.state.selectedServices.map { it.id },
            )
        )
    }

    private fun getCurrentDateTime(): LocalDateTime? {
        val date = dateSelector.state.date ?: return null
        val time = timeSelector.state.time ?: return null
        return date.atTime(time)
    }

    private fun isAllSelectorsSuccess(): Boolean {
        return clientsSelector.state.isSuccess && dateSelector.state.isSuccess && timeSelector.state.isSuccess && staffSelector.state.isSuccess && !storeState.isLoading
    }

    private fun onDate(date: LocalDate?) {
        val time = timeSelector.state.time ?: return
        val datetime = time.atDate(date ?: return)
        staffSelector.onChangeSchedule(datetime)
    }

    private fun onTime(time: LocalTime?) {
        val date = dateSelector.state.date ?: return
        val datetime = date.atTime(time ?: return)
        staffSelector.onChangeSchedule(datetime)
    }

}