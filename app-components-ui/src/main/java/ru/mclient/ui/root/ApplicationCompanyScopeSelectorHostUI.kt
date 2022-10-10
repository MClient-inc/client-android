package ru.mclient.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.root.ApplicationCompanyScopeSelectorHost
import ru.mclient.ui.company.list.CompaniesSelectorUI
import ru.mclient.ui.companynetwork.list.CompanyNetworksSelectorUI

@Composable
fun ApplicationCompanyScopeSelectorHostUI(
    component: ApplicationCompanyScopeSelectorHost,
    modifier: Modifier
) {
    Children(component.childStack) {
        ApplicationCompanyScopeSelectorNavHost(it.instance, modifier)
    }
}


@Composable
fun ApplicationCompanyScopeSelectorNavHost(
    child: ApplicationCompanyScopeSelectorHost.Child,
    modifier: Modifier,
) {
    when (child) {
        is ApplicationCompanyScopeSelectorHost.Child.Company ->
            CompaniesSelectorUI(component = child.component, modifier = modifier)

        is ApplicationCompanyScopeSelectorHost.Child.CompanyNetwork ->
            CompanyNetworksSelectorUI(component = child.component, modifier = modifier)
    }
}