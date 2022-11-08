package ru.mclient.common.abonement.clientcreate

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.clientcreate.AbonementClientCreateClientStore

class AbonementClientCreateClientComponent(
    componentContext: DIComponentContext,
    clientId: Long,
) : AbonementClientCreateClient, DIComponentContext by componentContext {

    private val store: AbonementClientCreateClientStore =
        getParameterizedStore { AbonementClientCreateClientStore.Params(clientId) }

    override val state: AbonementClientCreateClientState by store.states(this) { it.toState() }

    private fun AbonementClientCreateClientStore.State.toState(): AbonementClientCreateClientState {
        return AbonementClientCreateClientState(
            client = client?.let {
                AbonementClientCreateClientState.Client(
                    it.name,
                    it.phone,
                )
            },
            isLoading = isLoading,
        )
    }

}