package ru.mclient.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.mclient.common.root.ApplicationCompanyScopeSelectorHost
import ru.mclient.ui.LocalChildrenStackAnimator
import ru.mclient.ui.company.list.CompaniesSelectorUI
import ru.mclient.ui.companynetwork.list.CompanyNetworksSelectorUI

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ApplicationCompanyScopeSelectorHostUI(
    component: ApplicationCompanyScopeSelectorHost,
    modifier: Modifier
) {
    Children(component.childStack, animation = stackAnimation(LocalChildrenStackAnimator.current)) {
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