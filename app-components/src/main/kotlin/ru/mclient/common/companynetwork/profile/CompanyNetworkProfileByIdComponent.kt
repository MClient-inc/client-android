package ru.mclient.common.companynetwork.profile

import androidx.compose.runtime.State
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.companynetwork.profile.CompanyNetworkProfileStore

class CompanyNetworkProfileByIdComponent(
    componentContext: DIComponentContext,
    networkId: Long,
) : CompanyNetworkProfile, DIComponentContext by componentContext {

    private val store: CompanyNetworkProfileStore =
        getParameterizedStore { CompanyNetworkProfileStore.Params(networkId) }

    override val state: State<CompanyNetworkProfileState> = store.states(this) { it.toState() }

    private fun CompanyNetworkProfileStore.State.toState(): CompanyNetworkProfileState {
        return CompanyNetworkProfileState(
            network = network?.toState(),
            isLoading = isLoading,
            isFailure = isFailure,
        )
    }

    private fun CompanyNetworkProfileStore.State.CompanyNetwork.toState(): CompanyNetworkProfileState.CompanyNetwork {
        return CompanyNetworkProfileState.CompanyNetwork(
            title = title,
            codename = codename,
            description = description,
        )
    }

    override fun onRefresh() {
        store.accept(CompanyNetworkProfileStore.Intent.Refresh)
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

    override fun onCompany() {
        TODO("Not yet implemented")
    }

    override fun onAnalytics() {
        TODO("Not yet implemented")
    }

}