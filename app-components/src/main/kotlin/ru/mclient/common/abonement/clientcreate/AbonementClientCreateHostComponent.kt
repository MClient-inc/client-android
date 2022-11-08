package ru.mclient.common.abonement.clientcreate

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.clientcreate.AbonementClientCreateHostStore

class AbonementClientCreateHostComponent(
    componentContext: DIComponentContext,
    clientId: Long,
    companyId: Long,
    onSuccess: () -> Unit,
) : AbonementClientCreateHost, DIComponentContext by componentContext {

    private val store: AbonementClientCreateHostStore = getParameterizedStore {
        AbonementClientCreateHostStore.Params(
            companyId = companyId,
            clientId = clientId
        )
    }

    private val storeState by store.states(this) {
        if (it.isSuccess)
            onSuccess()
        it
    }

    override val state: AbonementClientCreateHostState
        get() = AbonementClientCreateHostState(
            isSuccess = storeState.isSuccess,
            isContinueAvailable = storeState.isAvailable && abonement.state.isSuccess,
            totalCost = abonement.state.abonement?.subabonement?.cost ?: 0
        )

    override val profile: AbonementClientCreateClient = AbonementClientCreateClientComponent(
        componentContext = childDIContext("abonement_client_create_client"),
        clientId = clientId,
    )

    override val abonement: AbonementClientCreateAbonementSelector =
        AbonementClientCreateAbonementSelectorComponent(
            componentContext = childDIContext("abonement_client_create_abonements"),
            companyId = companyId,
        )

    override fun onContinue() {
        val subabonement = abonement.state.abonement?.subabonement ?: return
        store.accept(AbonementClientCreateHostStore.Intent.Create(subabonement.id))
    }

    override val bar: TopBar = ImmutableTopBar(TopBarState("Абонемент клиента"))
}