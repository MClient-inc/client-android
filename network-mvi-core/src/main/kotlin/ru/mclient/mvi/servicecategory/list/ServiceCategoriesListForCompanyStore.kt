package ru.mclient.mvi.servicecategory.list

import ru.mclient.mvi.ParametrizedStore

interface ServiceCategoriesListForCompanyStore :
    ParametrizedStore<ServiceCategoriesListForCompanyStore.Intent, ServiceCategoriesListForCompanyStore.State, ServiceCategoriesListForCompanyStore.Label, ServiceCategoriesListForCompanyStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val categories: List<ServiceCategory>,
        val companyId: Long,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) {
        data class ServiceCategory(
            val id: Long,
            val title: String,
        )
    }

    sealed class Label

}