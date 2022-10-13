package ru.mclient.ui.company.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.company.profile.CompanyProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun CompanyProfileHostUI(
    component: CompanyProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
    ) {
        CompanyProfileUI(
            component = component.profile,
            modifier = modifier.padding(10.dp),
        )
    }
}