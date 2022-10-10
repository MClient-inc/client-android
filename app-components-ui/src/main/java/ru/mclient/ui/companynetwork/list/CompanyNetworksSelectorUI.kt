package ru.mclient.ui.companynetwork.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.companynetwork.list.CompanyNetworksSelector
import ru.mclient.ui.bar.TopBarHostUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNetworksSelectorUI(
    component: CompanyNetworksSelector,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
    ) {
        CompanyNetworksListUI(
            component = component.list,
            modifier = modifier,
        )
    }
}