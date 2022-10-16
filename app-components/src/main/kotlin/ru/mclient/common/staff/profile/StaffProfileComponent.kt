package ru.mclient.common.staff.profile

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.mvi.staff.profile.StaffProfileStore

class StaffProfileComponent(
    componentContext: DIComponentContext,
    staffId: Long,
) : StaffProfile, DIComponentContext by componentContext {


    private val store: StaffProfileStore =
        getParameterizedStore { StaffProfileStore.Params(staffId) }

    override val state: StateFlow<StaffProfileState> = store.states.map { it.toState() }
        .stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())

    private fun StaffProfileStore.State.toState(): StaffProfileState {
        return StaffProfileState(
            staff = staff?.toState(),
            isLoading = isLoading,
        )
    }

    private fun StaffProfileStore.State.Staff.toState(): StaffProfileState.Staff {
        return StaffProfileState.Staff(
            name = name, codename = codename
        )
    }

    override fun onRefresh() {
        store.accept(StaffProfileStore.Intent.Refresh)
    }

    override fun onEdit() {
        TODO("Not yet implemented")
    }

}