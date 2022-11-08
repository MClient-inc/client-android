package ru.mclient.common.client.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.profile.ClientProfileStore

class ClientProfileComponent(
    componentContext: DIComponentContext,
    clientId: Long,
    private val onAbonementCreate: () -> Unit,
    private val onQRCode: () -> Unit,
) : ClientProfile, DIComponentContext by componentContext {

    private val store: ClientProfileStore =
        getParameterizedStore { ClientProfileStore.Params(clientId) }

    override val state: ClientProfileState by store.states(this) { it.toState() }

    private fun ClientProfileStore.State.toState(): ClientProfileState {
        return ClientProfileState(
            client = client?.toState(),
            abonements = abonements?.map {
                ClientProfileState.ClientAbonement(
                    id = it.id,
                    usages = it.usages,
                    abonement = ClientProfileState.Abonement(
                        title = it.abonement.title,
                        subabonement = ClientProfileState.Subabonement(
                            title = it.abonement.subabonement.title,
                            maxUsages = it.abonement.subabonement.maxUsages
                        )
                    )
                )
            },
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

    override fun onQRCode() {
        onQRCode.invoke()
    }

    override fun onEdit() {
//        TODO("Not yet implemented")
    }

    override fun onAbonementCreate() {
        onAbonementCreate.invoke()
    }

}