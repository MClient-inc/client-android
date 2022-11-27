package ru.mclient.network.agreement


class GetAgreementOutput(
    val title: String,
    val content: String,
)

interface AgreementNetworkSource {

    suspend fun getUserAgreement(): GetAgreementOutput

    suspend fun getClientDataProcessingAgreement(): GetAgreementOutput

}