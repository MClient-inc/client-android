package ru.mclient.ui.service.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.service.profile.ServiceProfile
import ru.mclient.common.service.profile.ServiceProfileState
import ru.mclient.common.service.profile.ServiceProfileState.AnalyticsType.COMPANY
import ru.mclient.common.service.profile.ServiceProfileState.AnalyticsType.NETWORK

@Composable
fun ServiceProfileUI(
    component: ServiceProfile,
    modifier: Modifier,
) {
    ServiceProfilePage(
        state = component.state.toUI(),
        onEdit = component::onEdit,
        onRefresh = component::onRefresh,
        onDismiss = component::onDismiss,
        onToggleCompany = component::onToggleCompany,
        onToggleNetwork = component::onToggleNetwork,
        onSelect = component::onSelect,
        modifier = modifier
    )
}

fun ServiceProfileState.toUI(): ServiceProfilePageState {
    return ServiceProfilePageState(
        service = service?.toUI(),
        network = network?.toUI(),
        company = company?.toUI(),
        analyticsType = when (analyticsType) {
            COMPANY -> ServiceProfilePageState.AnalyticsType.COMPANY
            NETWORK -> ServiceProfilePageState.AnalyticsType.NETWORK
        },
        isTypeSelecting = isTypeSelecting,
        isRefreshing = service != null && isLoading,
        isLoading = isLoading,
    )
}

private fun ServiceProfileState.NetworkAnalytics.toUI(): ServiceProfilePageState.NetworkAnalytics {
    return ServiceProfilePageState.NetworkAnalytics(
        id = id,
        title = title,
        analytics = ServiceProfilePageState.AnalyticsItem(
            comeCount = analytics.comeCount,
            notComeCount = analytics.notComeCount,
            waitingCount = analytics.waitingCount,
            totalRecords = analytics.totalRecords,
            popularity = analytics.popularity,
        )
    )
}

private fun ServiceProfileState.CompanyAnalytics.toUI(): ServiceProfilePageState.CompanyAnalytics {
    return ServiceProfilePageState.CompanyAnalytics(
        id = id,
        title = title,
        analytics = ServiceProfilePageState.AnalyticsItem(
            comeCount = analytics.comeCount,
            notComeCount = analytics.notComeCount,
            waitingCount = analytics.waitingCount,
            totalRecords = analytics.totalRecords,
            popularity = analytics.popularity,
        )
    )
}

fun ServiceProfileState.Service.toUI(): ServiceProfilePageState.Service {
    return ServiceProfilePageState.Service(title, description, cost)
}