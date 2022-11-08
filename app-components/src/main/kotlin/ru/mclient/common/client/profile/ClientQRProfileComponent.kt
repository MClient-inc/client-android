package ru.mclient.common.client.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.ModalState
import ru.mclient.common.client.profile.ClientQRProfileState.Code
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.profile.ClientQRProfileStore

class ClientQRProfileComponent(
    componentContext: DIComponentContext,
    clientId: Long,
) : ClientQRProfile, DIComponentContext by componentContext {

    private val store: ClientQRProfileStore =
        getParameterizedStore { ClientQRProfileStore.Params(clientId) }

    override val state: ClientQRProfileState by store.states(this) { it.toState() }

    private fun ClientQRProfileStore.State.toState(): ClientQRProfileState {
        return ClientQRProfileState(
            code = code?.let(::Code),
            isLoading = isLoading,
            isShareAvailable = !isLoading,
        )
    }

    private fun ClientQRProfileStore.State.toModalState(): ModalState {
        return ModalState(isVisible)
    }

    override fun onShare() {
        TODO("Not yet implemented")
    }

    override val modalState: ModalState by store.states(this) { it.toModalState() }

    override fun updateState(isVisible: Boolean) {
        store.accept(ClientQRProfileStore.Intent.Move(isVisible))
    }

}