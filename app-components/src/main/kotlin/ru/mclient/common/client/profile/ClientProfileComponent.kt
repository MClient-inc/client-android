package ru.mclient.common.client.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.profile.ClientProfileStore

class ClientProfileComponent(
    componentContext: DIComponentContext,
    clientId: Long,
    private val onAbonementCreate: () -> Unit,
    private val onQRCode: () -> Unit,
    private val onRecord: (Long) -> Unit,
) : ClientProfile, DIComponentContext by componentContext {

    private val store: ClientProfileStore =
        getParameterizedStore { ClientProfileStore.Params(clientId) }

    override val state: ClientProfileState by store.states(this) { it.toState() }

    private fun ClientProfileStore.State.NetworkAnalytics.toState(): ClientProfileState.NetworkAnalytics {
        return ClientProfileState.NetworkAnalytics(
            id = id,
            title = title,
            analytics = ClientProfileState.ClientAnalyticsItem(
                notComeCount = analytics.notComeCount,
                comeCount = analytics.comeCount,
                waitingCount = analytics.waitingCount,
                totalCount = analytics.totalCount
            )
        )
    }

    private fun ClientProfileStore.State.CompanyAnalytics.toState(): ClientProfileState.CompanyAnalytics {
        return ClientProfileState.CompanyAnalytics(
            id = id,
            title = title,
            analytics = ClientProfileState.ClientAnalyticsItem(
                notComeCount = analytics.notComeCount,
                comeCount = analytics.comeCount,
                waitingCount = analytics.waitingCount,
                totalCount = analytics.totalCount
            )
        )
    }

    private fun ClientProfileStore.State.toState(): ClientProfileState {
        return ClientProfileState(
            client = client?.toState(),
            abonements = abonements?.map {
                ClientProfileState.ClientAbonement(
                    id = it.id,
                    usages = it.usages,
                    abonement = ClientProfileState.Abonement(
                        title = it.abonement.title,
                        subabonement = ClientProfileState.Subabonement(
                            title = it.abonement.subabonement.title,
                            maxUsages = it.abonement.subabonement.maxUsages
                        )
                    )
                )
            },
            networkAnalytics = networkAnalytics?.toState(),
            companyAnalytics = companyAnalytics?.toState(),
            records = records?.map { record ->
                ClientProfileState.Record(
                    id = record.id,
                    services = record.services.map { service ->
                        ClientProfileState.Service(
                            id = service.id,
                            title = service.title,
                            cost = service.cost
                        )
                    },
                    staff = ClientProfileState.Staff(
                        id = record.staff.id,
                        name = record.staff.name
                    ),
                    company = ClientProfileState.Company(
                        id = record.company.id,
                        title = record.company.title
                    ),
                    time = ClientProfileState.Time(
                        start = record.time.start,
                        end = record.time.end,
                        date = record.time.date
                    ),
                    totalCost = record.totalCost
                )
            },
            isLoading = isLoading,
        )
    }

    private fun ClientProfileStore.State.Client.toState(): ClientProfileState.Client {
        return ClientProfileState.Client(
            phone = phone, name = name
        )
    }

    override fun onRefresh() {
        store.accept(ClientProfileStore.Intent.Refresh)
    }

    override fun onQRCode() {
        onQRCode.invoke()
    }

    override fun onRecord(recordId: Long) {
        onRecord.invoke(recordId)
    }

    override fun onEdit() {
//        TODO("Not yet implemented")
    }

    override fun onAbonementCreate() {
        onAbonementCreate.invoke()
    }

}