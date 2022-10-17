package ru.mclient.common.staff.create

import androidx.compose.runtime.State
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.create.StaffCreateStore

class StaffCreateComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSuccess: (Long) -> Unit,
) : StaffCreate, DIComponentContext by componentContext {

    val store: StaffCreateStore = getParameterizedStore { StaffCreateStore.Param(companyId) }

    override val state: State<StaffCreateState> =
        store.states(this) { it.toState() }

    private fun StaffCreateStore.State.toState(): StaffCreateState {
        val staff = createdStaff
        if (staff != null) {
            onSuccess(staffId = staff.id)
        }
        return StaffCreateState(
            name = name,
            codename = codename,
            role = role,
            isLoading = isLoading,
            isError = isError
        )
    }

    private fun onSuccess(staffId: Long) {
        onSuccess.invoke(staffId)
    }

    override fun onUpdate(username: String, codename: String, role: String) {
        store.accept(StaffCreateStore.Intent.Update(username, codename, role))
    }

    override fun onCreate(username: String, codename: String, role: String) {
        store.accept(StaffCreateStore.Intent.Create(username, codename, ""))
    }

}