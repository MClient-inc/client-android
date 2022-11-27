package ru.mclient.common.auth.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import ru.mclient.common.DIComponentContext
import ru.mclient.common.agreement.AgreementModal
import ru.mclient.common.agreement.AgreementModalComponent
import ru.mclient.common.agreement.AgreementState
import ru.mclient.common.auth.AuthRequest
import ru.mclient.common.auth.AuthState
import ru.mclient.common.childDIContext

class DelegatingAuthRequestComponent(
    componentContext: DIComponentContext,
    private val onLogin: () -> Unit,
    private val onRegister: () -> Unit,
    state: AuthState,
) : AuthRequest, DIComponentContext by componentContext {

    override val state: AuthState by mutableStateOf(state)

    override val agreement: AgreementModal =
        AgreementModalComponent(
            componentContext = childDIContext("user_agreement_modal"),
            agreementType = AgreementState.Type.USER_AGREEMENT,
        )

    override fun onAgreement() {
        agreement.updateState(true)
    }

    override fun onLogin() {
        onLogin.invoke()
    }

    override fun onRegister() {
        onRegister.invoke()
    }
}