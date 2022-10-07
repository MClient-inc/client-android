package ru.mclient.common.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface MainHost {

    val childStack: Value<ChildStack<*, Child>>

    fun onHost()
    fun onLoyalty()
    fun onStorage()
    fun onCompany()

    sealed class Child {

        class Home(val component: ru.mclient.common.home.Home) : Child()

        class Loyalty(val component: ru.mclient.common.loyalty.Loyalty) : Child()

        class Storage(val component: ru.mclient.common.storage.Storage) : Child()

        class Company(val component: ru.mclient.common.company.Company): Child()

    }

}