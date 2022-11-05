package ru.mclient.common.company.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.company.profile.CompanyProfileStore

class CompanyProfileComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onStaff: () -> Unit,
    private val onNetwork: (Long) -> Unit,
    private val onServices: () -> Unit,
    private val onClients: () -> Unit,
) : CompanyProfile, DIComponentContext by componentContext {

    private val store: CompanyProfileStore =
        getParameterizedStore { CompanyProfileStore.Params(companyId) }

    override val state: CompanyProfileState by store.states(this) { it.toState() }


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
    }

    override fun onEdit() {
//        TODO("Not yet implemented")
    }

    override fun onClients() {
        onClients.invoke()
    }

    override fun onServices() {
        onServices.invoke()
    }

    override fun onStaff() {
        onStaff.invoke()
    }

    override fun onNetwork() {
        store.state.company?.networkId?.let(onNetwork)
    }
}