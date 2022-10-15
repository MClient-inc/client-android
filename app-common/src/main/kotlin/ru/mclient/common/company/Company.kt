package ru.mclient.common.company

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.company.profile.CompanyProfileHost
import ru.mclient.common.staff.list.StaffListHost
import ru.mclient.common.staff.profile.StaffProfileHost

interface Company {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {

        class CompanyProfile(val component: CompanyProfileHost) : Child()
        class StaffList(val component: StaffListHost) : Child()
        class StaffProfile(val component: StaffProfileHost) : Child()

    }

}