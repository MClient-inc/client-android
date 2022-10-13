package ru.mclient.common.company.create

import kotlinx.coroutines.flow.StateFlow

data class CompanyCreateState(
    val title: String,
    val codename: String,
    val description: String,
    val isLoading: Boolean,
    val isError : Boolean
)

interface CompanyCreate {

    val state: StateFlow<CompanyCreateState>

    fun onUpdate(title: String, codename: String, description: String)

    fun onCreate(title: String, codename: String, description: String)

}