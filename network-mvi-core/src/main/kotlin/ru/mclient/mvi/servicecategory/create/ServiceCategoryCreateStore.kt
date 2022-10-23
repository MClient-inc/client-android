package ru.mclient.mvi.servicecategory.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ServiceCategoryCreateStore :
    ParametrizedStore<ServiceCategoryCreateStore.Intent, ServiceCategoryCreateStore.State, Nothing, ServiceCategoryCreateStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        class Update(
            val title: String,
        ) : Intent()

        class Create(
            val title: String,
        ) : Intent()

    }

    @Parcelize
    data class State(
        val title: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val isSuccess: Boolean,
        val category: ServiceCategory?,
    ) : Parcelable {

        @Parcelize
        data class ServiceCategory(
            val id: Long,
            val title: String,
        ) : Parcelable

    }

}