package ru.mclient.app.ui.auth.host

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.mclient.app.ui.auth.login.LoginUI
import ru.mclient.app.ui.auth.register.RegisterUI
import ru.mclient.app.ui.auth.request.AuthRequestUI
import ru.mclient.common.auth.AuthHost

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
    }
}