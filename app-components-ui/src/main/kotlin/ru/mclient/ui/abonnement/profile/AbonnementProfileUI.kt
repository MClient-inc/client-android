package ru.mclient.ui.abonnement.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.profile.AbonnementProfile
import ru.mclient.common.abonement.profile.AbonnementProfileState


fun AbonnementProfileState.toUI(): AbonnementProfilePageState {
    return AbonnementProfilePageState(
        abonnement = abonnement?.toUI(),
        isRefreshing = isRefreshing,
        isLoading = isLoading
    )
}

fun AbonnementProfileState.Abonnement.toUI(): AbonnementProfilePageState.Abonnement {
    return AbonnementProfilePageState.Abonnement(
        services = services.map {
            AbonnementProfilePageState.Service(
                title = it.title,
                cost = it.cost
            )
        },
        subAbonnements = subAbonnements.map {
            AbonnementProfilePageState.SubAbonnement(
                name = it.name,
                maxTimesNumberToUse = it.maxTimesNumberToUse
            )
        },
        name = name
    )
}

@Composable
fun AbonnementProfileUI(
    component: AbonnementProfile,
    modifier: Modifier
) {
    val state = component.state
    AbonnementProfilePage(
        state = state.toUI(),
        onRefresh = component::onRefresh,
        modifier = modifier
    )

}