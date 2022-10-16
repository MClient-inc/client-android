package ru.mclient.common.staff.create

import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.create.StaffCreateStore

class StaffCreateComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : StaffCreate, DIComponentContext by componentContext {

    val store: StaffCreateStore = getParameterizedStore { StaffCreateStore.Param(companyId) }

    override val state: androidx.compose.runtime.State<StaffCreateState> =
        store.states(this) { it.toState() }

    private fun StaffCreateStore.State.toState(): StaffCreateState {
        return StaffCreateState(
            name = name,
            codename = codename,
            isLoading = isLoading,
            isError = isError
        )
    }

    override fun onUpdate(username: String, codename: String) {
        store.accept(StaffCreateStore.Intent.Update(username, codename, ""))
    }

    override fun onCreate(username: String, codename: String) {
        TODO("Not yet implemented")
    }
}