package ru.mclient.common.company

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.company.profile.CompanyProfileHostComponent
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileByIdHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.service.create.ServiceCreateHostComponent
import ru.mclient.common.service.list.ServiceListForCategoryAndCompanyHostComponent
import ru.mclient.common.service.profile.ServiceProfileHostComponent
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateHostComponent
import ru.mclient.common.servicecategory.list.ServiceCategoriesListHostForCompanyComponent
import ru.mclient.common.staff.StaffProfileHostComponent
import ru.mclient.common.staff.create.StaffCreateHostComponent
import ru.mclient.common.staff.list.StaffListForCompanyHostComponent
import ru.mclient.common.utils.states

class CompanyComponent(
    componentContext: DIComponentContext,
    applicationCompanyId: Long,
) : Company, DIComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val childStack: ChildStack<*, Company.Child> by diChildStack(
        source = navigation,
        initialConfiguration = Config.CompanyProfile(applicationCompanyId),
        handleBackButton = true,
        childFactory = this::createChild,
    ).states(this)

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

    private fun onNetwork(networkId: Long) {
        navigation.push(Config.CompanyNetwork(networkId))
    }

    private fun onServices(companyId: Long) {
        navigation.push(Config.ServiceCategories(companyId))
    }

    private fun onCategorySelected(categoryId: Long, companyId: Long) {
        navigation.push(Config.ServiceList(companyId = companyId, categoryId = categoryId))
    }

    private fun onCreateServiceCategory(companyId: Long) {
        navigation.push(Config.ServiceCategoryCreate(companyId))
    }

    private fun onServiceCategoryCreated(categoryId: Long) {
        navigation.pop()
    }

    private fun onCreateService(companyId: Long, categoryId: Long) {
        navigation.push(Config.ServiceCreate(companyId = companyId, categoryId = categoryId))
    }

    private fun onServiceCreated(serviceId: Long) {
        navigation.pop()
    }

    private fun createChild(config: Config, componentContext: DIComponentContext): Company.Child {
        return when (config) {
            is Config.CompanyProfile -> Company.Child.CompanyProfile(
                CompanyProfileHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onStaff = { onStaffFromCompany(config.companyId) },
                    onNetwork = { onNetwork(it) },
                    onServices = { onServices(config.companyId) },
                )
            )

            is Config.StaffFromCompany -> Company.Child.StaffList(
                StaffListForCompanyHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSelect = ::onStaff,
                    onCreate = { onCreateStaff(config.companyId) },
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
                    onSuccess = { onStaff(it, true) },
                )
            )

            is Config.CompanyNetwork -> Company.Child.CompanyNetwork(
                CompanyNetworkProfileByIdHostComponent(
                    componentContext = componentContext,
                    networkId = config.networkId,
                )
            )

            is Config.ServiceCategories -> Company.Child.ServiceCategoriesList(
                ServiceCategoriesListHostForCompanyComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onCategorySelected = {
                        onCategorySelected(
                            categoryId = it,
                            companyId = config.companyId,
                        )
                    },
                    onCreate = { onCreateServiceCategory(config.companyId) }
                )
            )

            is Config.ServiceList -> Company.Child.ServiceList(
                ServiceListForCategoryAndCompanyHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    categoryId = config.categoryId,
                    onCreate = { onCreateService(config.companyId, config.categoryId) }
                )
            )

            is Config.ServiceCategoryCreate -> Company.Child.ServiceCategoryCreate(
                ServiceCategoryCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onCreated = { onServiceCategoryCreated(it.id) },
                )
            )

            is Config.ServiceCreate -> Company.Child.ServiceCreate(
                ServiceCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    categoryId = config.categoryId,
                    onCreated = { onServiceCreated(it.id) }
                )
            )

            is Config.ServiceProfile -> Company.Child.ServiceProfile(
                ServiceProfileHostComponent(
                    componentContext = componentContext,
                    serviceId = config.serviceId
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

        @Parcelize
        data class CompanyNetwork(val networkId: Long) : Config()

        @Parcelize
        data class ServiceCategories(val companyId: Long) : Config()

        @Parcelize
        data class ServiceList(val companyId: Long, val categoryId: Long) : Config()

        @Parcelize
        data class ServiceCreate(val companyId: Long, val categoryId: Long) : Config()

        @Parcelize
        data class ServiceCategoryCreate(val companyId: Long) : Config()

        @Parcelize
        data class ServiceProfile(val serviceId: Long): Config()

    }

}