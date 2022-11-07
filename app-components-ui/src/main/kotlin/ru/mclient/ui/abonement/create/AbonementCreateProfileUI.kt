package ru.mclient.ui.abonement.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.create.AbonementCreateProfile
import ru.mclient.common.abonement.create.AbonementCreateProfileState


@Composable
fun AbonementCreateProfileUI(
    component: AbonementCreateProfile,
    modifier: Modifier,
) {
    AbonementCreateProfileBlock(
        state = component.state.toUI(),
        onUpdate = { component.onUpdate(it.title) },
        modifier = modifier
    )
}

private fun AbonementCreateProfileState.toUI(): AbonementCreateProfileBlockState {
    return AbonementCreateProfileBlockState(
        title = title,
    )
}
