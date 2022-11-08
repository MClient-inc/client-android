package ru.mclient.ui.abonement.clientcreate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateAbonementSelector
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateAbonementSelectorState
import ru.mclient.ui.abonement.list.AbonementsListUI


private fun AbonementClientCreateAbonementSelectorState.toUI(): AbonementClientCreateAbonementSelectorBlockState {
    return AbonementClientCreateAbonementSelectorBlockState(
        isExpanded = isExpanded,
        isAvailable = isAvailable,
        abonement = abonement?.let {
            AbonementClientCreateAbonementSelectorBlockState.Abonement(
                id = it.id,
                title = it.title,
                subabonement = AbonementClientCreateAbonementSelectorBlockState.Subabonement(
                    id = it.subabonement.id,
                    title = it.subabonement.title,
                )
            )
        }
    )
}

@Composable
fun AbonementClientCreateAbonementSelectorUI(
    component: AbonementClientCreateAbonementSelector,
    modifier: Modifier,
) {
    AbonementClientCreateAbonementSelectorBlock(
        state = component.state.toUI(),
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        modifier = modifier,
    ) {
        AbonementsListUI(
            component = component.selector,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 350.dp)
        )
    }
}