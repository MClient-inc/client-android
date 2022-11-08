package ru.mclient.ui.abonement.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.list.AbonementsList
import ru.mclient.common.abonement.list.AbonementsListState

@Composable
fun AbonementsListUI(
    component: AbonementsList,
    modifier: Modifier,
) {
    AbonementsListPage(
        state = component.state.toUI(),
        onClickAbonement = { component.onSelect(it.id) },
        onClickSubabonement = { abonement, subabonement ->
            component.onSelect(
                abonement.id,
                subabonement.id
            )
        },
        onRefresh = component::onRefresh,
        modifier = modifier,
    )
}

private fun AbonementsListState.toUI(): AbonementsListPageState {
    return AbonementsListPageState(
        abonements = abonements.map { abonement ->
            AbonementsListPageState.Abonement(
                id = abonement.id,
                title = abonement.title,
                subabonements = abonement.subabonements.map { subabonement ->
                    AbonementsListPageState.Subabonement(subabonement.id, subabonement.title)
                }
            )
        },
        isSubabonementClickable = isSubabonementClickable,
        isAbonementClickable = isAbonementClickable,
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        isFailure = isFailure,
    )
}