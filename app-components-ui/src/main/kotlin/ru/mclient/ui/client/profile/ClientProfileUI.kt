package ru.mclient.ui.client.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.profile.ClientProfile
import ru.mclient.common.client.profile.ClientProfileState
import ru.mclient.common.client.profile.ClientProfileState.RecordStatus.COME
import ru.mclient.common.client.profile.ClientProfileState.RecordStatus.NOT_COME
import ru.mclient.common.client.profile.ClientProfileState.RecordStatus.WAITING

@Composable
fun ClientProfileUI(
    component: ClientProfile,
    modifier: Modifier,
) {
    ClientProfilePage(
        state = component.state.toUI(),
        onEdit = component::onEdit,
        onRefresh = component::onRefresh,
        onCreateAbonement = component::onAbonementCreate,
        onQRCode = component::onQRCode,
        onRecord = component::onRecord,
        modifier = modifier,
    )
}

private fun ClientProfileState.NetworkAnalytics.toState(): ClientProfilePageState.NetworkAnalytics {
    return ClientProfilePageState.NetworkAnalytics(
        id = id,
        title = title,
        analytics = ClientProfilePageState.ClientAnalyticsItem(
            comeCount = this.analytics.comeCount,
            notComeCount = this.analytics.notComeCount,
            waitingCount = this.analytics.waitingCount,
            totalCount = this.analytics.totalCount
        )
    )
}

private fun ClientProfileState.CompanyAnalytics.toState(): ClientProfilePageState.CompanyAnalytics {
    return ClientProfilePageState.CompanyAnalytics(
        id = id,
        title = title,
        analytics = ClientProfilePageState.ClientAnalyticsItem(
            comeCount = this.analytics.comeCount,
            notComeCount = this.analytics.notComeCount,
            waitingCount = this.analytics.waitingCount,
            totalCount = this.analytics.totalCount
        )
    )
}

fun ClientProfileState.toUI(): ClientProfilePageState {
    return ClientProfilePageState(
        profile = client?.toUI(),
        isRefreshing = client != null && isLoading,
        isLoading = isLoading,
        abonements = abonements?.map {
            ClientProfilePageState.ClientAbonement(
                id = it.id,
                usages = it.usages,
                abonement = ClientProfilePageState.Abonement(
                    title = it.abonement.title,
                    subabonement = ClientProfilePageState.Subabonement(
                        title = it.abonement.subabonement.title,
                        maxUsages = it.abonement.subabonement.maxUsages
                    )
                )
            )
        },
        networkAnalytics = networkAnalytics?.toState(),
        companyAnalytics = companyAnalytics?.toState(),
        records = records?.map {
            ClientProfilePageState.Record(
                id = it.id,
                time = ClientProfilePageState.Time(
                    start = it.time.start,
                    end = it.time.end,
                    date = it.time.date
                ),
                company = ClientProfilePageState.Company(
                    id = it.company.id,
                    title = it.company.title
                ),
                services = it.services.map { service ->
                    ClientProfilePageState.Service(
                        id = service.id, cost = service.cost, title = service.title
                    )
                },
                totalCost = it.totalCost,
                staff = ClientProfilePageState.Staff(
                    id = it.staff.id,
                    name = it.staff.name
                ),
                status = when (it.status) {
                    WAITING -> ClientProfilePageState.RecordStatus.WAITING
                    COME -> ClientProfilePageState.RecordStatus.COME
                    NOT_COME -> ClientProfilePageState.RecordStatus.NOT_COME
                }
            )
        }

    )
}

fun ClientProfileState.Client.toUI(): ClientProfilePageState.Profile {
    return ClientProfilePageState.Profile(name, phone)
}