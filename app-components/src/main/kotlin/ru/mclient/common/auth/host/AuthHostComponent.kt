package ru.mclient.common.auth.host

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import kotlinx.parcelize.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.auth.AuthHost
import ru.mclient.common.auth.AuthState
import ru.mclient.common.auth.oauth.OAuthLogin
import ru.mclient.common.auth.register.WorkingRegisterComponent
import ru.mclient.common.auth.request.DelegatingAuthRequestComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.utils.states

class AuthHostComponent(
    componentContext: DIComponentContext,
    private val onAuthorized: (Long) -> Unit,
) : AuthHost, DIComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()


    private val register = WorkingRegisterComponent()

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext
    ): AuthHost.Child {
        return when (config) {
            is Config.Login -> AuthHost.Child.ExternalLogin(
                OAuthLogin(
                    componentContext = componentContext,
                    onAuthorized = onAuthorized
                )
            )

            is Config.Register -> AuthHost.Child.Register(register)
            is Config.Request -> AuthHost.Child.Request(
                DelegatingAuthRequestComponent(
                    componentContext = componentContext,
                    onLogin = ::onLogin,
                    onRegister = ::onRegister,
                    state = AuthState(
                        isLoginAvailable = true,
                        isRegisterAvailable = true
                    )
                )
            )
        }
    }

    private fun onLogin() {
        navigator.bringToFront(Config.Login)
    }


    private fun onRegister() {
        navigator.bringToFront(Config.Register)
    }

    override val childStack: ChildStack<*, AuthHost.Child> by diChildStack(
        source = navigator,
        initialConfiguration = Config.Request,
        handleBackButton = true,
        childFactory = ::createChild
    ).states(this)


    sealed class Config : Parcelable {

        @Parcelize
        object Login : Config()

        @Parcelize
        object Register : Config()

        @Parcelize
        object Request : Config()

    }

}