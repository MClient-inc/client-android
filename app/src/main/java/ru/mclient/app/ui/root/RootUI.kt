package ru.mclient.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.app.ui.auth.host.AuthHostUI
import ru.mclient.common.root.Root

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
    }
}