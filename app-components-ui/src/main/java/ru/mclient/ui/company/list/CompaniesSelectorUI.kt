package ru.mclient.ui.company.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.company.list.CompaniesSelector
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun CompaniesSelectorUI(
    component: CompaniesSelector,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
    ) {
        CompaniesListUI(
            component = component.list,
            modifier = modifier,
        )
    }
}