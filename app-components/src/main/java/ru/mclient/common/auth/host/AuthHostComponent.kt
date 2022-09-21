package ru.mclient.common.auth.host

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.ktor.client.*
import kotlinx.parcelize.Parcelize
import ru.mclient.common.auth.AuthHost
import ru.mclient.common.auth.AuthState
import ru.mclient.common.auth.login.WorkingLoginComponent
import ru.mclient.common.auth.register.WorkingRegisterComponent
import ru.mclient.common.auth.request.DelegatingAuthRequestComponent

class AuthHostComponent(
    componentContext: ComponentContext,
    client: HttpClient,
) : AuthHost, ComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()

    private val login = WorkingLoginComponent(
        componentContext = childContext("login"),
        client = client,
    )

    private val register = WorkingRegisterComponent(
        componentContext = childContext("register"),
        client = client,
    )

    private fun createChild(config: Config, componentContext: ComponentContext): AuthHost.Child {
        return when (config) {
            is Config.Login -> AuthHost.Child.Login(login)
            is Config.Register -> AuthHost.Child.Register(register)
            is Config.Request -> AuthHost.Child.Request(
                DelegatingAuthRequestComponent(
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

    override val childStack: Value<ChildStack<*, AuthHost.Child>> = childStack(
        source = navigator,
        initialConfiguration = Config.Request,
        handleBackButton = true,
        childFactory = ::createChild
    )


    sealed class Config : Parcelable {

        @Parcelize
        object Login : Config()

        @Parcelize
        object Register : Config()

        @Parcelize
        object Request : Config()

    }

}