package ru.mclient.common.company

import com.arkivanov.decompose.router.stack.ChildStack
import ru.mclient.common.company.profile.CompanyProfileHost
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileHost
import ru.mclient.common.service.list.ServiceListHost
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateHost
import ru.mclient.common.servicecategory.list.ServiceCategoriesListHost
import ru.mclient.common.staff.create.StaffCreateHost
import ru.mclient.common.staff.list.StaffListHost
import ru.mclient.common.staff.profile.StaffProfileHost

interface Company {

    val childStack: ChildStack<*, Child>

    sealed class Child {

        class CompanyProfile(val component: CompanyProfileHost) : Child()

        class StaffList(val component: StaffListHost) : Child()

        class StaffProfile(val component: StaffProfileHost) : Child()

        class StaffCreate(val component: StaffCreateHost) : Child()

        class CompanyNetwork(val component: CompanyNetworkProfileHost) : Child()

        class ServiceCategoriesList(val component: ServiceCategoriesListHost) : Child()

        class ServiceCategoryCreate(val component: ServiceCategoryCreateHost) : Child()

        class ServiceList(val component: ServiceListHost) : Child()

    }

}