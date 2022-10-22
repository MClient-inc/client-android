package ru.mclient.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import ru.mclient.common.company.list.CompaniesSelector
import ru.mclient.common.companynetwork.list.CompanyNetworksSelector

interface ApplicationCompanyScopeSelectorHost {

    val childStack: ChildStack<*, Child>


    sealed class Child {

        class CompanyNetwork(val component: CompanyNetworksSelector) : Child()

        class Company(val component: CompaniesSelector) : Child()

    }


}