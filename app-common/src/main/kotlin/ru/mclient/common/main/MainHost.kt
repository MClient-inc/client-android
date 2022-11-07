package ru.mclient.common.main

import com.arkivanov.decompose.router.stack.ChildStack

interface MainHost {

    val childStack: ChildStack<*, Child>

    fun onHost()
    fun onLoyalty()
    fun onStorage()
    fun onCompany()

    sealed interface Child {

        @JvmInline
        value class Home(val component: ru.mclient.common.home.Home) : Child

        @JvmInline
        value class Loyalty(val component: ru.mclient.common.loyalty.Loyalty) : Child


        @JvmInline
        value class Storage(val component: ru.mclient.common.storage.Storage) : Child


        @JvmInline
        value class Company(val component: ru.mclient.common.company.Company) : Child

    }

}