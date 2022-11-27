package ru.mclient.common.agreement

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.ModalState
import ru.mclient.common.childDIContext
import ru.mclient.common.modal.toState
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.modal.ModalStore

class AgreementModalComponent(
    componentContext: DIComponentContext,
    agreementType: AgreementState.Type,
) : AgreementModal, DIComponentContext by componentContext {

    private val store: ModalStore = getSavedStateStore("user_agreement_modal")

    override val agreement =
        AgreementComponent(
            componentContext = childDIContext(key = "user_agreement_child"),
            agreementType = agreementType,
        )

    override val modalState: ModalState by store.states(this) { it.toState() }

    override fun updateState(isVisible: Boolean) {
        store.accept(ModalStore.Intent(isVisible))
        agreement.onLoad()
    }
}