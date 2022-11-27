package ru.mclient.common.client.create

import ru.mclient.common.agreement.AgreementModal

data class ClientCreateState(
    val name: String,
    val phone: String,
    val isLoading: Boolean,
    val error: String?,
)

interface ClientCreate {

    val agreement: AgreementModal

    val state: ClientCreateState

    fun onUpdate(name: String, phone: String)

    fun onCreate()

    fun onAgreement()

}