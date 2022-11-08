package ru.mclient.ui.client.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.profile.ClientProfile
import ru.mclient.common.client.profile.ClientProfileState

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
        modifier = modifier,
    )
}

fun ClientProfileState.toUI(): ClientProfilePageState {
    return ClientProfilePageState(
        profile = client?.toUI(),
        isRefreshing = client != null && isLoading,
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
        isLoading = isLoading
    )
}

fun ClientProfileState.Client.toUI(): ClientProfilePageState.Profile {
    return ClientProfilePageState.Profile(name, phone)
}