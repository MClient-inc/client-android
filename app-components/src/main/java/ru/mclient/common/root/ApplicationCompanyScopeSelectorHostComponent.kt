package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.list.CompaniesSelectorComponent
import ru.mclient.common.companynetwork.list.CompanyNetworksSelectorComponent
import ru.mclient.common.diChildStack

class ApplicationCompanyScopeSelectorHostComponent(
    componentContext: DIComponentContext,
    accountId: Long,
    private val onSelect: (Long) -> Unit,
) : ApplicationCompanyScopeSelectorHost, DIComponentContext by componentContext {

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext
    ): ApplicationCompanyScopeSelectorHost.Child {
        return when (config) {
            is Config.Company -> ApplicationCompanyScopeSelectorHost.Child.Company(
                CompaniesSelectorComponent(
                    componentContext = componentContext,
                    networkId = config.networkId,
                    onSelect = { onSelect(it.id) }
                )
            )

            is Config.CompanyNetwork -> ApplicationCompanyScopeSelectorHost.Child.CompanyNetwork(
                CompanyNetworksSelectorComponent(
                    componentContext = componentContext,
                    accountId = config.accountId,
                    onSelect = { navigation.bringToFront(Config.Company(it.id)) }
                )
            )
        }
    }

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, ApplicationCompanyScopeSelectorHost.Child>> =
        diChildStack(
            source = navigation,
            initialConfiguration = Config.CompanyNetwork(accountId),
            handleBackButton = true,
            childFactory = this::createChild,
        )

    sealed class Config : Parcelable {

        @Parcelize
        data class CompanyNetwork(val accountId: Long) : Config()

        @Parcelize
        data class Company(val networkId: Long) : Config()

    }

}