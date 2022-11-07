package ru.mclient.ui.loyalty

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.loyalty.Loyalty
import ru.mclient.ui.abonement.create.AbonementCreateHostUI
import ru.mclient.ui.abonement.list.AbonementsListHostUI
import ru.mclient.ui.abonement.profile.AbonementProfileHostUI

@Composable
fun LoyaltyUI(component: Loyalty, modifier: Modifier) {
    Children(stack = component.childStack) {
        LoyaltyNavHost(child = it.instance, modifier = modifier)
    }
}

@Composable
fun LoyaltyNavHost(child: Loyalty.Child, modifier: Modifier) {
    when (child) {
        is Loyalty.Child.AbonementsList ->
            AbonementsListHostUI(child.component, modifier)

        is Loyalty.Child.AbonementsProfile -> AbonementProfileHostUI(
            component = child.component,
            modifier = modifier,
        )

        is Loyalty.Child.AbonementCreate -> AbonementCreateHostUI(
            component = child.component,
            modifier = modifier,
        )
    }
}