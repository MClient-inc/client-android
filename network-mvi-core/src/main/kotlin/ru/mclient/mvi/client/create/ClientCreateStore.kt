package ru.mclient.mvi.client.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ClientCreateStore :
    ParametrizedStore<ClientCreateStore.Intent, ClientCreateStore.State, Nothing, ClientCreateStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        class Update(
            val name: String,
            val phone: String,
        ) : Intent()

        object Create : Intent()

    }

    @Parcelize
    data class State(
        val name: String,
        val phone: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val isSuccess: Boolean,
        val client: Client?,
    ) : Parcelable {

        @Parcelize
        data class Client(
            val id: Long,
            val name: String,
            val phone: String,
        ) : Parcelable

    }

}