package ru.mclient.common.company.create

data class CompanyCreateState(
    val title: String,
    val codename: String,
    val description: String,
    val isLoading: Boolean,
    val isError: Boolean
)

interface CompanyCreate {

    val state: CompanyCreateState

    fun onUpdate(title: String, codename: String, description: String)

    fun onCreate(title: String, codename: String, description: String)

}