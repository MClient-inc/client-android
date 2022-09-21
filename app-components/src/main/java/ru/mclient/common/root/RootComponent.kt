package ru.mclient.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ktor.client.*
import ru.mclient.common.auth.host.AuthHostComponent

class RootComponent(
    componentContext: ComponentContext,
    private val client: HttpClient,
) : Root, ComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Root.Child>> = childStack(
        source = navigator,
        initialConfiguration = Config.Auth,
        childFactory = this::createChild
    )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): Root.Child {
        return when (config) {
            is Config.Auth -> {
                Root.Child.Auth(
                    AuthHostComponent(
                        componentContext = componentContext,
                        client = client
                    )
                )
            }
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        object Auth : Config()

    }

}