package ru.mclient.common.company.profile

import android.util.Log
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.mvi.company.profile.CompanyProfileStore

class CompanyProfileComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : CompanyProfile, DIComponentContext by componentContext {

    private val store: CompanyProfileStore =
        getParameterizedStore { CompanyProfileStore.Params(companyId) }

    override val state: StateFlow<CompanyProfileState> = store.states.map { it.toState() }
        .stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())


    private fun CompanyProfileStore.State.toState(): CompanyProfileState {
        return CompanyProfileState(
            profile = company?.toState(),
            isLoading = isLoading,
            isFailure = isFailure,
        )
    }

    private fun CompanyProfileStore.State.Company.toState(): CompanyProfileState.Profile {
        return CompanyProfileState.Profile(
            title = title,
            codename = codename,
            description = description,
        )
    }

    override fun onRefresh() {
        store.accept(CompanyProfileStore.Intent.Refresh)
        Log.d("AcceptTester", "end")
    }

    override fun onEdit() {
        TODO("Not yet implemented")
    }

    override fun onClients() {
        TODO("Not yet implemented")
    }

    override fun onServices() {
        TODO("Not yet implemented")
    }

    override fun onStaff() {
        TODO("Not yet implemented")
    }

    override fun onNetwork() {
        TODO("Not yet implemented")
    }
}