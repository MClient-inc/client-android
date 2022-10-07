package ru.mclient.common.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface AuthHost {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {

        class Login(val component: ru.mclient.common.auth.Login) : Child()
        class ExternalLogin(val component: ru.mclient.common.auth.ExternalLogin) : Child()

        class Request(val component: AuthRequest) : Child()

        class Register(val component: ru.mclient.common.auth.Register) : Child()

    }

}