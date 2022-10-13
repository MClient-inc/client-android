package ru.mclient.ui.auth.host

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.mclient.common.auth.AuthHost
import ru.mclient.ui.auth.login.LoginUI
import ru.mclient.ui.auth.oauth.ExternalLoginUI
import ru.mclient.ui.auth.register.RegisterUI
import ru.mclient.ui.auth.request.AuthRequestUI

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AuthHostUI(
    component: AuthHost,
    modifier: Modifier,
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        AuthHostNavHost(instance = it.instance, modifier = modifier)
    }
}

@Composable
fun AuthHostNavHost(instance: AuthHost.Child, modifier: Modifier) {
    when (instance) {
        is AuthHost.Child.Request -> AuthRequestUI(
            component = instance.component,
            modifier = modifier,
        )

        is AuthHost.Child.Register -> RegisterUI(
            component = instance.component,
            modifier = modifier,
        )

        is AuthHost.Child.Login -> LoginUI(
            component = instance.component,
            modifier = modifier,
        )

        is AuthHost.Child.ExternalLogin -> ExternalLoginUI(
            component = instance.component,
            modifier = modifier,
        )
    }
}