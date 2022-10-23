package ru.mclient.ui.companynetwork.profile

import androidx.compose.foundation.layout.fillMaxSize
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
        modifier = modifier,
    ) {
        CompanyNetworkProfileUI(
            component = component.profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
        )
    }
}