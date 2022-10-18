package ru.mclient.ui.companynetwork.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun CompanyNetworkProfileHostUI(
    component: CompanyNetworkProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
    ) {
        CompanyNetworkProfileUI(
            component = component.profile,
            modifier = modifier.padding(10.dp),
        )
    }
}