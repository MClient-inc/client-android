package ru.mclient.common.client.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.profile.ClientProfileStore

class ClientProfileComponent(
    componentContext: DIComponentContext,
    clientId: Long
) : ClientProfile, DIComponentContext by componentContext {


    private val store: ClientProfileStore =
        getParameterizedStore { ClientProfileStore.Params(clientId) }

    override val state: ClientProfileState by store.states(this) { it.toState() }

    private fun ClientProfileStore.State.toState(): ClientProfileState {
        return ClientProfileState(
            client = client?.toState(),
            isLoading = isLoading,
        )
    }

    private fun ClientProfileStore.State.Client.toState(): ClientProfileState.Client {
        return ClientProfileState.Client(
            phone = phone, name = name
        )
    }

    override fun onRefresh() {
        store.accept(ClientProfileStore.Intent.Refresh)
    }

    override fun onEdit() {
//        TODO("Not yet implemented")
    }

}