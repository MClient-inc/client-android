package ru.mclient.mvi.service.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ServiceCreateStore :
    ParametrizedStore<ServiceCreateStore.Intent, ServiceCreateStore.State, Nothing, ServiceCreateStore.Params> {

    data class Params(
        val companyId: Long,
        val categoryId: Long,
    )

    sealed class Intent {

        class Update(
            val title: String,
            val description: String,
            val cost: String,
        ) : Intent()

        object Create : Intent()

    }

    @Parcelize
    data class State(
        val title: String,
        val description: String,
        val cost: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val isSuccess: Boolean,
        val service: Service?,
    ) : Parcelable {

        @Parcelize
        data class Service(
            val id: Long,
            val title: String,
            val description: String,
            val cost: String,
        ) : Parcelable

    }

}