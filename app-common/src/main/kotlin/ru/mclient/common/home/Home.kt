package ru.mclient.common.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateHost
import ru.mclient.common.client.profile.ClientProfileHost
import ru.mclient.common.home.block.HomeBlockHost
import ru.mclient.common.record.create.RecordCreateHost
import ru.mclient.common.record.list.RecordsListHost
import ru.mclient.common.record.profile.RecordProfileHost


interface Home {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {

        class HomeBlock(val component: HomeBlockHost) : Child()

        class ClientProfile(val component: ClientProfileHost) : Child()

        class AbonementCreate(val component: AbonementClientCreateHost) : Child()

        class RecordsList(val component: RecordsListHost) : Child()

        class RecordCreate(val component: RecordCreateHost) : Child()

        class RecordProfile(val component: RecordProfileHost) : Child()

    }

}