package ru.mclient.common.company

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.profile.CompanyProfileHostComponent
import ru.mclient.common.diChildStack

class CompanyComponent(
    componentContext: DIComponentContext,
    applicationCompanyId: Long,
) : Company, DIComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Company.Child>> = diChildStack(
        source = navigation,
        initialConfiguration = Config.CompanyProfile(applicationCompanyId),
        childFactory = this::createChild,
    )


    private fun createChild(config: Config, componentContext: DIComponentContext): Company.Child {
        return when (config) {
            is Config.CompanyProfile -> Company.Child.CompanyProfile(
                CompanyProfileHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId
                )
            )
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        data class CompanyProfile(val companyId: Long) : Config()

    }

}