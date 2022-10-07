package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.auth.AuthHost
import ru.mclient.common.main.MainHost

interface Root {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {

        class Auth(val component: AuthHost) : Child()

        class Main(val component: MainHost) : Child()

    }

}