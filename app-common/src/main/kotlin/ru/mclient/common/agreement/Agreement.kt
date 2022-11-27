package ru.mclient.common.agreement

class AgreementState(
    val title: String?,
    val content: String?,
) {

    enum class Type {
        USER_AGREEMENT, CLIENT_DATA_PROCESSING_AGREEMENT,
    }
}

interface Agreement {

    val state: AgreementState

}