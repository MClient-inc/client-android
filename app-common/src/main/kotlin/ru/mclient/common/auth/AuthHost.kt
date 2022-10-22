package ru.mclient.common.auth

import com.arkivanov.decompose.router.stack.ChildStack

interface AuthHost {

    val childStack: ChildStack<*, Child>

    sealed class Child {

        class Login(val component: ru.mclient.common.auth.Login) : Child()
        class ExternalLogin(val component: ru.mclient.common.auth.ExternalLogin) : Child()

        class Request(val component: AuthRequest) : Child()

        class Register(val component: ru.mclient.common.auth.Register) : Child()

    }

}