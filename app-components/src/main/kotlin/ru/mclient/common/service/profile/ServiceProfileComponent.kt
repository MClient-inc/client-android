package ru.mclient.common.service.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.service.profile.ServiceProfileStore

class ServiceProfileComponent(
    componentContext: DIComponentContext,
    serviceId: Long
) : ServiceProfile, DIComponentContext by componentContext {

    private val store: ServiceProfileStore =
        getParameterizedStore { ServiceProfileStore.Params(serviceId) }

    override val state: ServiceProfileState by store.states(this) { it.toState() }

    private fun ServiceProfileStore.State.toState(): ServiceProfileState {
        return ServiceProfileState(
            service = service?.toState(),
            isLoading = isLoading
        )
    }

    private fun ServiceProfileStore.State.Service.toState(): ServiceProfileState.Service {
        return ServiceProfileState.Service(
            title = title,
            cost = description,
            description = cost
        )
    }

    override fun onRefresh() {
        store.accept(ServiceProfileStore.Intent.Refresh)
    }

    override fun onEdit() {
        TODO()
    }

}