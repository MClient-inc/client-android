package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import ru.mclient.common.auth.AuthHost
import ru.mclient.common.main.MainHost
import ru.mclient.common.splash.SplashHost

interface Root {

    val childStack: ChildStack<*, Child>

    val isSplashShown: Boolean

    sealed class Child {

        class Auth(val component: AuthHost) : Child()

        class Main(val component: MainHost) : Child()

        class Splash(val component: SplashHost) : Child()

        class ApplicationScope(val component: ApplicationCompanyScopeSelectorHost) : Child()

    }

}