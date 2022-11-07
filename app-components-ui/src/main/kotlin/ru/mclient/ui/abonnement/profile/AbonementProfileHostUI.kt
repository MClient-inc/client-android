package ru.mclient.ui.abonnement.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.abonement.profile.AbonnementProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun AbonnementProfileHostUI(
    component: AbonnementProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        AbonnementProfileUI(
            component = component.abonnementProfile,
            modifier = Modifier.fillMaxSize()
        )
    }
}