package ru.mclient.common.client.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.agreement.AgreementModalComponent
import ru.mclient.common.agreement.AgreementState.Type.CLIENT_DATA_PROCESSING_AGREEMENT
import ru.mclient.common.childDIContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.create.ClientCreateStore

class ClientCreateComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSuccess: (Long) -> Unit,
) : ClientCreate, DIComponentContext by componentContext {

    override val agreement = AgreementModalComponent(
        componentContext = childDIContext("client_agreement"),
        agreementType = CLIENT_DATA_PROCESSING_AGREEMENT,
    )

    private val store: ClientCreateStore =
        getParameterizedStore { ClientCreateStore.Params(companyId) }

    override val state: ClientCreateState by store.states(this) { it.toState() }

    private fun onSuccess(client: ClientCreateStore.State.Client) {
        onSuccess.invoke(client.id)
    }

    private fun ClientCreateStore.State.toState(): ClientCreateState {
        val client = client
        if (isSuccess && client != null)
            onSuccess(client)
        return ClientCreateState(
            name = name,
            phone = phone,
            isLoading = isLoading,
            error = if (isError) "Неизветная ошибка" else null,
        )
    }

    override fun onUpdate(name: String, phone: String) {
        store.accept(ClientCreateStore.Intent.Update(name, phone))
    }

    override fun onCreate() {
        store.accept(ClientCreateStore.Intent.Create)
    }

    override fun onAgreement() {
        agreement.updateState(true)
    }

}