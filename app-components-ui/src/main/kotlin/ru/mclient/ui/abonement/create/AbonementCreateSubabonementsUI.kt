package ru.mclient.ui.abonement.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.create.AbonementCreateSubabonements
import ru.mclient.common.abonement.create.AbonementCreateSubabonementsState

private fun AbonementCreateSubabonementsState.toUI(): AbonementCreateSubabonementsBlockState {
    return AbonementCreateSubabonementsBlockState(
        subabonements = subabonements.map {
            AbonementCreateSubabonementsBlockState.Subabonement(
                it.title,
                it.usages.toString(),
                it.uniqueId,
            )
        },
        creation = AbonementCreateSubabonementsBlockState.SubabonementCreation(
            title = creation.title,
            usages = if (creation.usages == 0) "" else creation.usages.toString(),
            isAvailable = creation.isAvailable,
            isButtonAvailable = creation.isButtonAvailable
        )
    )
}

@Composable
fun AbonementCreateSubabonementsUI(
    component: AbonementCreateSubabonements,
    modifier: Modifier,
) {
    AbonementCreateSubabonementsBlock(
        state = component.state.toUI(),
        onCreate = component::onCreate,
        onUpdate = { component.onUpdate(it.title, it.usage) },
        onDelete = { component.onDelete(it.uniqueId) },
        modifier = modifier
    )
}