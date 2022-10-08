package ru.mclient.common.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.CompanyComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.home.HomeComponent
import ru.mclient.common.loyalty.LoyaltyComponent
import ru.mclient.common.storage.StorageComponent

class MainHostComponent(
    componentContext: DIComponentContext
) : MainHost, DIComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, MainHost.Child>> = diChildStack(
        source = navigation,
        initialConfiguration = Config.Home,
        childFactory = this::createChild,
    )

    override fun onHost() {
        navigation.bringToFront(Config.Home)
    }

    override fun onLoyalty() {
        navigation.bringToFront(Config.Loyalty)
    }

    override fun onStorage() {
        navigation.bringToFront(Config.Storage)
    }

    override fun onCompany() {
        navigation.bringToFront(Config.Company)
    }

    private fun createChild(config: Config, componentContext: DIComponentContext): MainHost.Child {
        return when (config) {
            is Config.Company -> MainHost.Child.Company(CompanyComponent())
            is Config.Loyalty -> MainHost.Child.Loyalty(LoyaltyComponent())
            is Config.Home -> MainHost.Child.Home(HomeComponent())
            is Config.Storage -> MainHost.Child.Storage(StorageComponent())
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        object Home : Config()

        @Parcelize
        object Loyalty : Config()

        @Parcelize
        object Storage : Config()

        @Parcelize
        object Company : Config()

    }

}