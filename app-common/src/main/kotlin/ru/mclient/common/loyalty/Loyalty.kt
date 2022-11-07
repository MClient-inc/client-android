package ru.mclient.common.loyalty

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.abonement.create.AbonementCreateHost
import ru.mclient.common.abonement.list.AbonementsListHost
import ru.mclient.common.abonement.profile.AbonementProfileHost

interface Loyalty {

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {

        @JvmInline
        value class AbonementsList(val component: AbonementsListHost) : Child

        @JvmInline
        value class AbonementsProfile(val component: AbonementProfileHost) : Child

        @JvmInline
        value class AbonementCreate(val component: AbonementCreateHost) : Child

    }

}