package ru.mclient.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.ui.auth.host.AuthHostUI
import ru.mclient.common.root.Root
import ru.mclient.ui.main.MainUI

@Composable
fun RootUI(component: Root, modifier: Modifier) {
    Children(stack = component.childStack) {
        RootNavHost(it.instance, modifier)
    }
}

@Composable
fun RootNavHost(instance: Root.Child, modifier: Modifier) {
    when (instance) {
        is Root.Child.Auth -> AuthHostUI(component = instance.component, modifier = modifier)
        is Root.Child.Main -> MainUI(component = instance.component, modifier = modifier)
    }
}