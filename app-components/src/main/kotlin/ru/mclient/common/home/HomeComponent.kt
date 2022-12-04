package ru.mclient.common.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateHostComponent
import ru.mclient.common.client.profile.ClientProfileHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.home.block.HomeBlockComponent
import ru.mclient.common.record.create.RecordCreateHostComponent
import ru.mclient.common.record.list.RecordsListHostComponent
import ru.mclient.common.record.profile.RecordProfileHostComponent

class HomeComponent(
    componentContext: DIComponentContext,
    private val companyId: Long,
) : Home, DIComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private fun onRecordsList(companyId: Long) {
        navigation.push(Config.RecordsList(companyId))
    }

    private fun onRecordCreate(companyId: Long) {
        navigation.push(Config.RecordCreate(companyId))
    }

    private fun onRecordCreated() {
        navigation.pop()
    }

    private fun onRecordSelect(recordId: Long) {
        navigation.push(Config.RecordProfile(recordId))
    }


    override val childStack: Value<ChildStack<*, Home.Child>> =
        diChildStack(
            source = navigation,
            initialConfiguration = Config.HomeBlock(companyId),
            handleBackButton = true,
            childFactory = this::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext,
    ): Home.Child {
        return when (config) {
            is Config.HomeBlock ->
                Home.Child.HomeBlock(
                    HomeBlockComponent(
                        componentContext = componentContext,
                        companyId = config.companyId,
                        onSelectRecord = { onRecordSelect(it) },
                        onRecordsList = { onRecordsList(config.companyId) },
                        onClient = { navigation.push(Config.ClientProfile(it)) }
                    )
                )

            is Config.RecordsList ->
                Home.Child.RecordsList(
                    RecordsListHostComponent(
                        componentContext = componentContext,
                        companyId = config.companyId,
                        onRecordCreate = { onRecordCreate(config.companyId) },
                        onSelect = { onRecordSelect(it.id) }
                    )
                )

            is Config.RecordCreate ->
                Home.Child.RecordCreate(
                    RecordCreateHostComponent(
                        componentContext = componentContext,
                        companyId = config.companyId,
                        onSuccess = { onRecordCreated() }
                    )
                )

            is Config.RecordProfile ->
                Home.Child.RecordProfile(
                    RecordProfileHostComponent(
                        componentContext = componentContext,
                        recordId = config.recordId
                    )
                )

            is Config.AbonementCreate ->
                Home.Child.AbonementCreate(
                    AbonementClientCreateHostComponent(
                        componentContext = componentContext,
                        companyId = companyId,
                        clientId = config.clientId,
                        onSuccess = navigation::pop
                    )
                )

            is Config.ClientProfile ->
                Home.Child.ClientProfile(
                    ClientProfileHostComponent(
                        componentContext = componentContext,
                        clientId = config.clientId,
                        onAbonementCreate = { navigation.push(Config.AbonementCreate(config.clientId)) },
                        onRecord = { onRecordSelect(it) }
                    )
                )
        }
    }


    sealed interface Config : Parcelable {

        @Parcelize
        @JvmInline
        value class HomeBlock(val companyId: Long) : Config

        @Parcelize
        @JvmInline
        value class RecordsList(val companyId: Long) : Config

        @Parcelize
        @JvmInline
        value class RecordCreate(val companyId: Long) : Config

        @Parcelize
        @JvmInline
        value class RecordProfile(val recordId: Long) : Config

        @Parcelize
        @JvmInline
        value class ClientProfile(val clientId: Long) : Config

        @Parcelize
        @JvmInline
        value class AbonementCreate(val clientId: Long) : Config

    }

}