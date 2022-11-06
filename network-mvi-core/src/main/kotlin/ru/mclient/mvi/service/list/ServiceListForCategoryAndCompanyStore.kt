package ru.mclient.mvi.service.list

import ru.mclient.mvi.ParametrizedStore

interface ServiceListForCategoryAndCompanyStore :
    ParametrizedStore<ServiceListForCategoryAndCompanyStore.Intent, ServiceListForCategoryAndCompanyStore.State, ServiceListForCategoryAndCompanyStore.Label, ServiceListForCategoryAndCompanyStore.Params> {

    data class Params(
        val categoryId: Long,
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val category: ServiceCategory?,
        val services: List<Service>,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) {
        data class Service(
            val id: Long,
            val title: String,
            val cost: Long,
            val formattedCost: String,
        )

        data class ServiceCategory(
            val id: Long,
            val title: String,
        )
    }

    sealed class Label

}