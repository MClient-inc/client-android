package ru.mclient.ui.abonement.clientcreate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateClient
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateClientState

@Composable
fun AbonementClientCreateClientUI(
    component: AbonementClientCreateClient,
    modifier: Modifier,
) {
    AbonementClientCreateClientBlock(
        state = component.state.toUI(),
        modifier = modifier
    )
}

private fun AbonementClientCreateClientState.toUI(): AbonementClientCreateClientBlockState {
    return AbonementClientCreateClientBlockState(
        client = client?.let {
            AbonementClientCreateClientBlockState.Client(
                name = it.name,
                phone = it.phone,
            )
        },
        isLoading = isLoading,
    )
}
