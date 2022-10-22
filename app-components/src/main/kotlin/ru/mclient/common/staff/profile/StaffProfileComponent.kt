package ru.mclient.common.staff.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.profile.StaffProfileStore

class StaffProfileComponent(
    componentContext: DIComponentContext,
    staffId: Long,
) : StaffProfile, DIComponentContext by componentContext {


    private val store: StaffProfileStore =
        getParameterizedStore { StaffProfileStore.Params(staffId) }

    override val state: StaffProfileState by store.states(this) { it.toState() }

    private fun StaffProfileStore.State.toState(): StaffProfileState {
        return StaffProfileState(
            staff = staff?.toState(),
            isLoading = isLoading,
        )
    }

    private fun StaffProfileStore.State.Staff.toState(): StaffProfileState.Staff {
        return StaffProfileState.Staff(
            name = name, codename = codename, role = role
        )
    }

    override fun onRefresh() {
        store.accept(StaffProfileStore.Intent.Refresh)
    }

    override fun onEdit() {
        TODO("Not yet implemented")
    }

}