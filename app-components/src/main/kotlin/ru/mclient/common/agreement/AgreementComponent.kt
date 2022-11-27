package ru.mclient.common.agreement

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.agreement.AgreementState.Type.CLIENT_DATA_PROCESSING_AGREEMENT
import ru.mclient.common.agreement.AgreementState.Type.USER_AGREEMENT
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.agreement.AgreementStore

class AgreementComponent(
    componentContext: DIComponentContext,
    agreementType: AgreementState.Type,
) : Agreement, DIComponentContext by componentContext {

    private val store: AgreementStore =
        getParameterizedStore { AgreementStore.Param(type = agreementType.toStore()) }

    private fun AgreementState.Type.toStore(): AgreementStore.Param.AgreementType {
        return when (this) {
            USER_AGREEMENT -> AgreementStore.Param.AgreementType.USER_AGREEMENT
            CLIENT_DATA_PROCESSING_AGREEMENT -> AgreementStore.Param.AgreementType.CLIENT_DATA_PROCESSING_AGREEMENT
        }
    }

    override val state: AgreementState by store.states(this) { it.toState() }

    private fun AgreementStore.State.toState(): AgreementState {
        return AgreementState(
            title = title, content = content,
        )
    }


    fun onLoad() {
        store.accept(AgreementStore.Intent.Load)
    }

}