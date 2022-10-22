package ru.mclient.common.root

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.auth.host.AuthHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.main.MainHostComponent
import ru.mclient.common.splash.SplashComponent
import ru.mclient.common.utils.states

class RootComponent(
    componentContext: DIComponentContext,
) : Root, DIComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()

    override val childStack: ChildStack<*, Root.Child> by diChildStack(
        source = navigator,
        initialConfiguration = Config.Splash,
        childFactory = this::createChild
    ).states(this)

    override val isSplashShown: Boolean get() = childStack.active.instance is Root.Child.Splash

    private fun onAuthenticated(accountId: Long) {
        navigator.navigate { listOf(Config.ApplicationScope(accountId = accountId)) }
    }

    private fun onUnauthenticated() {
        navigator.navigate { listOf(Config.Auth) }
    }

    private fun onSelected(
        accountId: Long,
        companyId: Long,
    ) {
        navigator.navigate { listOf(Config.Main(accountId = accountId, companyId = companyId)) }
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
                        applicationCompanyId = config.companyId,
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

            is Config.ApplicationScope -> Root.Child.ApplicationScope(
                ApplicationCompanyScopeSelectorHostComponent(
                    componentContext = componentContext,
                    accountId = config.accountId,
                    onSelect = { onSelected(config.accountId, it) })
            )
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        object Auth : Config()

        @Parcelize
        data class Main(
            val accountId: Long,
            val companyId: Long,
        ) : Config()

        @Parcelize
        object Splash : Config()

        @Parcelize
        class ApplicationScope(
            val accountId: Long,
        ) : Config()

    }

}