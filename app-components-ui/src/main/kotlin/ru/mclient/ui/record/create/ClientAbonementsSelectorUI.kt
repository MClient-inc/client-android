package ru.mclient.ui.record.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.profile.ClientAbonementsSelector
import ru.mclient.common.record.profile.ClientCreateAbonementsState

private fun ClientCreateAbonementsState.toUI(): RecordCreateAbonementsSelectorBlockState {
    return RecordCreateAbonementsSelectorBlockState(
        isExpanded,
        isAvailable,
        selectedAbonements = selectedAbonements.mapValues { (_, abonement) ->
            RecordCreateAbonementsSelectorBlockState.ClientAbonement(
                id = abonement.id,
                usages = abonement.usages,
                abonement = RecordCreateAbonementsSelectorBlockState.Abonement(
                    id = abonement.abonement.id,
                    title = abonement.abonement.title,
                    subabonement = RecordCreateAbonementsSelectorBlockState.Subabonement(
                        id = abonement.abonement.subabonement.id,
                        title = abonement.abonement.subabonement.title,
                        maxUsages = abonement.abonement.subabonement.maxUsages,
                        cost = abonement.abonement.subabonement.cost,
                    )
                )
            )

        },
        clientAbonements = clientAbonements.map { abonement ->
            RecordCreateAbonementsSelectorBlockState.ClientAbonement(
                id = abonement.id,
                usages = abonement.usages,
                abonement = RecordCreateAbonementsSelectorBlockState.Abonement(
                    id = abonement.abonement.id,
                    title = abonement.abonement.title,
                    subabonement = RecordCreateAbonementsSelectorBlockState.Subabonement(
                        id = abonement.abonement.subabonement.id,
                        title = abonement.abonement.subabonement.title,
                        maxUsages = abonement.abonement.subabonement.maxUsages,
                        cost = abonement.abonement.subabonement.cost,
                    )
                )
            )
        },
        isRefreshing = isRefreshing,
    )

}

@Composable
fun ClientAbonementsSelectorUI(
    component: ClientAbonementsSelector,
    modifier: Modifier,
) {
    RecordCreateAbonementsSelectorBlock(
        state = component.state.toUI(),
        onSelect = { component.onSelect(it.id) },
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        onDelete = component::onDelete,
        onRefresh = component::onRefresh,
        modifier = modifier,
    )
}