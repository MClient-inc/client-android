package ru.mclient.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.mclient.common.home.Home
import ru.mclient.ui.LocalChildrenStackAnimator
import ru.mclient.ui.abonement.clientcreate.AbonementClientCreateHostUI
import ru.mclient.ui.client.profile.ClientProfileHostUI
import ru.mclient.ui.record.create.RecordCreateHostUI
import ru.mclient.ui.record.list.RecordsListHostUI
import ru.mclient.ui.record.profile.RecordProfileHostUI

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeUI(component: Home, modifier: Modifier) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(LocalChildrenStackAnimator.current),
    ) {
        HomeNavHost(child = it.instance, modifier = modifier)
    }
}

@Composable
fun HomeNavHost(child: Home.Child, modifier: Modifier) {
    when (child) {
        is Home.Child.HomeBlock ->
            HomeBlockHostUI(component = child.component, modifier = modifier)

        is Home.Child.RecordsList ->
            RecordsListHostUI(component = child.component, modifier = modifier)

        is Home.Child.RecordCreate ->
            RecordCreateHostUI(component = child.component, modifier = modifier)

        is Home.Child.RecordProfile ->
            RecordProfileHostUI(component = child.component, modifier = modifier)

        is Home.Child.AbonementCreate ->
            AbonementClientCreateHostUI(component = child.component, modifier = modifier)

        is Home.Child.ClientProfile ->
            ClientProfileHostUI(component = child.component, modifier = modifier)
    }
}