package ru.mclient.common.client.profile

import ru.mclient.common.Modal


data class ClientQRProfileState(
    val code: Code?,
    val isLoading: Boolean,
    val isShareAvailable: Boolean,
) {

    class Code(
        val code: String,
    )

}

interface ClientQRProfile : Modal {

    val state: ClientQRProfileState

    fun onShare()

}