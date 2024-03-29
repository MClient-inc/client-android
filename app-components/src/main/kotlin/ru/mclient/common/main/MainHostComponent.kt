package ru.mclient.common.main

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.CompanyComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.home.HomeComponent
import ru.mclient.common.loyalty.LoyaltyComponent
import ru.mclient.common.storage.StorageComponent
import ru.mclient.common.utils.states

class MainHostComponent(
    componentContext: DIComponentContext,
    private val applicationCompanyId: Long,
) : MainHost, DIComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: ChildStack<*, MainHost.Child> by diChildStack(
        source = navigation,
        initialConfiguration = Config.Home(applicationCompanyId),
        childFactory = this::createChild,
    ).states(this)

    override fun onHost() {
        navigation.bringToFront(Config.Home(applicationCompanyId))
    }

    override fun onLoyalty() {
        navigation.bringToFront(Config.Loyalty(applicationCompanyId))
    }

    override fun onStorage() {
        navigation.bringToFront(Config.Storage)
    }

    override fun onCompany() {
        navigation.bringToFront(Config.Company(applicationCompanyId))
    }

    private fun createChild(config: Config, componentContext: DIComponentContext): MainHost.Child {
        return when (config) {
            is Config.Company -> MainHost.Child.Company(
                CompanyComponent(
                    componentContext,
                    config.companyId,
                )
            )

            is Config.Loyalty -> MainHost.Child.Loyalty(
                LoyaltyComponent(
                    componentContext,
                    config.companyId
                )
            )

            is Config.Home -> MainHost.Child.Home(HomeComponent(componentContext, config.companyId))
            is Config.Storage -> MainHost.Child.Storage(StorageComponent())
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        @JvmInline
        value class Home(val companyId: Long) : Config

        @Parcelize
        @JvmInline
        value class Loyalty(val companyId: Long) : Config


        @Parcelize
        object Storage : Config

        @Parcelize
        @JvmInline
        value class Company(val companyId: Long) : Config

    }

}