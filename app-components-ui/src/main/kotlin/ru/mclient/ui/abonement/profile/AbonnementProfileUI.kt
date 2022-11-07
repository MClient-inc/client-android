package ru.mclient.ui.abonement.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.profile.AbonementProfile
import ru.mclient.common.abonement.profile.AbonementProfileState


fun AbonementProfileState.toUI(): AbonementProfilePageState {
    return AbonementProfilePageState(
        abonement = abonement?.toUI(),
        isRefreshing = isRefreshing,
        isLoading = isLoading
    )
}

fun AbonementProfileState.Abonement.toUI(): AbonementProfilePageState.Abonement {
    return AbonementProfilePageState.Abonement(
        services = services.map {
            AbonementProfilePageState.Service(
                title = it.title,
                cost = it.cost
            )
        },
        subabonements = subabonements.map {
            AbonementProfilePageState.SubAbonement(
                title = it.title,
                maxTimesNumberToUse = it.maxTimesNumberToUse
            )
        },
        title = title,
    )
}

@Composable
fun AbonementProfileUI(
    component: AbonementProfile,
    modifier: Modifier,
) {
    val state = component.state
    AbonementProfilePage(
        state = state.toUI(),
        onRefresh = component::onRefresh,
        modifier = modifier
    )
}