package ru.mclient.ui.abonement.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.abonement.profile.AbonementProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun AbonementProfileHostUI(
    component: AbonementProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        AbonementProfileUI(
            component = component.abonementProfile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        )
    }
}