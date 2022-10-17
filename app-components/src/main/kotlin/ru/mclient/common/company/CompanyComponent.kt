package ru.mclient.common.company

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.profile.CompanyProfileHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.staff.StaffProfileHostComponent
import ru.mclient.common.staff.create.StaffCreateHostComponent
import ru.mclient.common.staff.list.StaffListForCompanyHostComponent

class CompanyComponent(
    componentContext: DIComponentContext,
    applicationCompanyId: Long,
) : Company, DIComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Company.Child>> = diChildStack(
        source = navigation,
        initialConfiguration = Config.CompanyProfile(applicationCompanyId),
        handleBackButton = true,
        childFactory = this::createChild,
    )

    private fun onStaffFromCompany(companyId: Long) {
        navigation.push(Config.StaffFromCompany(companyId))
    }

    private fun onStaff(staffId: Long, replaceCurrent: Boolean = false) {
        if (replaceCurrent) {
            navigation.replaceCurrent(Config.StaffProfile(staffId))
        } else {
            navigation.push(Config.StaffProfile(staffId))
        }
    }

    private fun onCreateStaff(companyId: Long) {
        navigation.push(Config.CreateStaff(companyId))
    }

    private fun createChild(config: Config, componentContext: DIComponentContext): Company.Child {
        return when (config) {
            is Config.CompanyProfile -> Company.Child.CompanyProfile(
                CompanyProfileHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onStaff = { onStaffFromCompany(config.companyId) }
                )
            )

            is Config.StaffFromCompany -> Company.Child.StaffList(
                StaffListForCompanyHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSelect = ::onStaff,
                    onCreate = { onCreateStaff(config.companyId) }
                )
            )

            is Config.StaffProfile -> Company.Child.StaffProfile(
                StaffProfileHostComponent(
                    componentContext = componentContext,
                    staffId = config.staffId,
                )
            )

            is Config.CreateStaff -> Company.Child.StaffCreate(
                StaffCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSuccess = { onStaff(it, true) }
                )
            )
        }
    }

    sealed class Config : Parcelable {

        @Parcelize
        data class CompanyProfile(val companyId: Long) : Config()

        @Parcelize
        data class StaffFromCompany(val companyId: Long) : Config()

        @Parcelize
        data class StaffProfile(val staffId: Long) : Config()

        @Parcelize
        data class CreateStaff(val companyId: Long) : Config()

    }

}