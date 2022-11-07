package ru.mclient.common.loyalty

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.abonement.create.AbonementCreateHostComponent
import ru.mclient.common.abonement.list.AbonementsListHostComponent
import ru.mclient.common.abonement.profile.AbonementProfileHostComponent
import ru.mclient.common.diChildStack

class LoyaltyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : Loyalty, DIComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Loyalty.Child>> = diChildStack(
        source = navigation,
        initialConfiguration = Config.AbonementsList(companyId),
        handleBackButton = true,
        childFactory = this::createChild,
    )

    private fun onAbonementSelect(abonementId: Long) {
        navigation.push(Config.AbonementProfile(abonementId))
    }

    private fun onAbonementCreate(companyId: Long) {
        navigation.push(Config.AbonementCreate(companyId))
    }

    private fun onAbonementCreated(abonementId: Long) {
        navigation.replaceCurrent(Config.AbonementProfile(abonementId))
    }

    private fun createChild(config: Config, componentContext: DIComponentContext): Loyalty.Child {
        return when (config) {
            is Config.AbonementsList -> Loyalty.Child.AbonementsList(
                AbonementsListHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSelect = this::onAbonementSelect,
                    onCreate = { onAbonementCreate(config.companyId) },
                )
            )

            is Config.AbonementProfile -> Loyalty.Child.AbonementsProfile(
                AbonementProfileHostComponent(
                    componentContext = componentContext,
                    abonementId = config.abonementId
                )
            )

            is Config.AbonementCreate -> Loyalty.Child.AbonementCreate(
                AbonementCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSuccess = ::onAbonementCreated,
                )
            )
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        @JvmInline
        value class AbonementsList(val companyId: Long) : Config

        @Parcelize
        @JvmInline
        value class AbonementProfile(val abonementId: Long) : Config

        @Parcelize
        @JvmInline
        value class AbonementCreate(val companyId: Long) : Config

    }

}