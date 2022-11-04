package ru.mclient.common.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.home.block.HomeBlockHost
import ru.mclient.common.record.create.RecordCreateHost
import ru.mclient.common.record.list.RecordsListHost


interface Home {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {

        class HomeBlock(val component: HomeBlockHost) : Child()

        class RecordsList(val component: RecordsListHost) : Child()

        class RecordCreate(val component: RecordCreateHost) : Child()

    }

}