package ru.mclient.common.staff.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.list.StaffListForCompanyStore

class StaffListForCompanyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSelect: (Long) -> Unit,
    private val onCreate: () -> Unit,
) : StaffList, DIComponentContext by componentContext {

    private val store: StaffListForCompanyStore =
        getParameterizedStore { StaffListForCompanyStore.Params(companyId) }

    private fun StaffListForCompanyStore.State.toState(): StaffListState {
        return StaffListState(
            staff = staff.map {
                StaffListState.Staff(
                    id = it.id,
                    name = it.name,
                    codename = it.codename,
                    icon = it.icon,
                    role = it.role,
                )
            },
            isLoading = isLoading,
        )
    }

    override val state: StaffListState by store.states(this) { it.toState() }


    override fun onRefresh() {
        store.accept(StaffListForCompanyStore.Intent.Refresh)
    }

    override fun onSelect(staffId: Long) {
        onSelect.invoke(staffId)
    }

    override fun onCreateStaff() {
        onCreate.invoke()
    }

}