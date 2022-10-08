package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.auth.host.AuthHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.main.MainHostComponent
import ru.mclient.common.splash.SplashComponent

class RootComponent(
    componentContext: DIComponentContext,
) : Root, DIComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Root.Child>> = diChildStack(
        source = navigator,
        initialConfiguration = Config.Splash,
        childFactory = this::createChild
    )

    override val isSplashShown: Boolean = childStack.active.instance is Root.Child.Splash

    private fun onAuthenticated() {
        navigator.navigate { listOf(Config.Main) }
    }

    private fun onUnauthenticated() {
        navigator.navigate { listOf(Config.Auth) }
    }

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext,
    ): Root.Child {
        return when (config) {
            is Config.Auth -> {
                Root.Child.Auth(
                    AuthHostComponent(
                        componentContext = componentContext,
                        onAuthorized = this::onAuthenticated,
                    )
                )
            }

            is Config.Main -> {
                Root.Child.Main(
                    MainHostComponent(
                        componentContext = componentContext,
                    )
                )
            }

            Config.Splash -> Root.Child.Splash(
                SplashComponent(
                    componentContext = componentContext,
                    onAuthenticated = this::onAuthenticated,
                    onUnauthenticated = this::onUnauthenticated,
                )
            )
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        object Auth : Config()

        @Parcelize
        object Main : Config()

        @Parcelize
        object Splash : Config()

    }

}