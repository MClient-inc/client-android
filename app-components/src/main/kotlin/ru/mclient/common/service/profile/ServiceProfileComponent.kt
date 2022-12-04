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

    private fun ServiceProfileStore.State.NetworkAnalytics.toState(): ServiceProfileState.NetworkAnalytics {
        return ServiceProfileState.NetworkAnalytics(
            id = id,
            title = title,
            analytics = ServiceProfileState.AnalyticsItem(
                comeCount = analytics.comeCount,
                notComeCount = analytics.notComeCount,
                waitingCount = analytics.waitingCount,
                totalRecords = analytics.totalRecords,
                value = analytics.value
            )
        )
    }

    private fun ServiceProfileStore.State.CompanyAnalytics.toState(): ServiceProfileState.CompanyAnalytics {
        return ServiceProfileState.CompanyAnalytics(
            id = id,
            title = title,
            analytics = ServiceProfileState.AnalyticsItem(
                comeCount = analytics.comeCount,
                notComeCount = analytics.notComeCount,
                waitingCount = analytics.waitingCount,
                totalRecords = analytics.totalRecords,
                value = analytics.value
            )
        )
    }

    private fun ServiceProfileStore.State.toState(): ServiceProfileState {
        return ServiceProfileState(
            service = service?.toState(),
            network = network?.toState(),
            company = company?.toState(),
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