package ru.mclient.mvi.company.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface CompanyCreateStore : Store<CompanyCreateStore.Intent, CompanyCreateStore.State, Nothing> {

    sealed class Intent {

        class Update(
            val title: String,
            val codename: String,
            val description: String,
        ) : Intent()

        class Create(
            val title: String,
            val codename: String,
            val description: String,
        ) : Intent()

    }

    @Parcelize
    data class State(
        val title: String,
        val codename: String,
        val description: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val isSuccess: Boolean,
    ) : Parcelable


}