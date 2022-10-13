package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.company.list.CompaniesSelector
import ru.mclient.common.companynetwork.list.CompanyNetworksSelector

interface ApplicationCompanyScopeSelectorHost {

    val childStack: Value<ChildStack<*, Child>>


    sealed class Child {

        class CompanyNetwork(val component: CompanyNetworksSelector) : Child()

        class Company(val component: CompaniesSelector) : Child()

    }


}