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
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateHostComponent
import ru.mclient.common.client.create.ClientCreateHostComponent
import ru.mclient.common.client.list.ClientsListForCompanyHostComponent
import ru.mclient.common.client.profile.ClientProfileHostComponent
import ru.mclient.common.company.profile.CompanyProfileHostComponent
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileByIdHostComponent
import ru.mclient.common.diChildStack
import ru.mclient.common.record.profile.RecordProfileHostComponent
import ru.mclient.common.service.create.ServiceCreateHostComponent
import ru.mclient.common.service.list.ServiceListForCategoryAndCompanyHostComponent
import ru.mclient.common.service.profile.ServiceProfileHostComponent
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateHostComponent
import ru.mclient.common.servicecategory.list.ServiceCategoriesListHostForCompanyComponent
import ru.mclient.common.staff.create.StaffCreateHostComponent
import ru.mclient.common.staff.list.StaffListForCompanyHostComponent
import ru.mclient.common.staff.profile.StaffProfileHostComponent
import ru.mclient.common.staff.schedule.StaffScheduleHostComponent
import ru.mclient.common.utils.states

class CompanyComponent(
    componentContext: DIComponentContext,
    private val applicationCompanyId: Long,
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

    private fun onClients(companyId: Long) {
        navigation.push(Config.ClientsList(companyId))
    }

    private fun onServiceSelect(serviceId: Long) {
        navigation.push(Config.ServiceProfile(serviceId))
    }

    private fun onClient(clientId: Long) {
        navigation.push(Config.ClientProfile(clientId))
    }

    private fun onClientCreate(companyId: Long) {
        navigation.push(Config.ClientCreate(companyId))
    }

    private fun onClientCreated() {
        navigation.pop()
    }

    private fun onEditSchedule(staffId: Long) {
        navigation.push(Config.StaffScheduleEdit(staffId))
    }

    private fun onEditScheduleSuccess(staffId: Long) {
        navigation.pop()
    }

    private fun onAbonementCreatedSuccess() {
        navigation.pop()
    }

    private fun onAbonementClientCreate(clientId: Long) {
        navigation.push(
            Config.ClientAbonementCreate(
                companyId = applicationCompanyId,
                clientId = clientId,
            )
        )
    }

    private fun onRecordSelect(recordId: Long) {
        navigation.push(Config.RecordProfile(recordId))
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
                    onClients = { onClients(config.companyId) },
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
                    onEditSchedule = { onEditSchedule(config.staffId) },
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
                    onCreate = { onCreateService(config.companyId, config.categoryId) },
                    onSelect = this::onServiceSelect
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

            is Config.ClientsList -> Company.Child.ClientsList(
                ClientsListForCompanyHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onCreate = { onClientCreate(config.companyId) },
                    onClient = { onClient(it.id) }
                )
            )

            is Config.ClientProfile -> Company.Child.ClientProfile(
                ClientProfileHostComponent(
                    componentContext = componentContext,
                    clientId = config.clientId,
                    onAbonementCreate = { onAbonementClientCreate(config.clientId) },
                    onRecord = { onRecordSelect(it) }
                )
            )

            is Config.StaffScheduleEdit -> Company.Child.StaffSchedule(
                StaffScheduleHostComponent(
                    componentContext = componentContext,
                    staffId = config.staffId,
                    onSuccess = { onEditScheduleSuccess(config.staffId) }
                )
            )

            is Config.ClientCreate -> Company.Child.ClientCreate(
                ClientCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    onSuccess = ::onClientCreated,
                )
            )

            is Config.ClientAbonementCreate -> Company.Child.ClientAbonementCreate(
                AbonementClientCreateHostComponent(
                    componentContext = componentContext,
                    companyId = config.companyId,
                    clientId = config.clientId,
                    onSuccess = ::onAbonementCreatedSuccess,
                )
            )

            is Config.RecordProfile -> Company.Child.RecordProfile(
                RecordProfileHostComponent(
                    componentContext = componentContext,
                    recordId = config.recordId
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
        data class StaffScheduleEdit(val staffId: Long) : Config()

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
        data class ServiceProfile(val serviceId: Long) : Config()

        @Parcelize
        data class ClientsList(val companyId: Long) : Config()

        @Parcelize
        data class ClientProfile(val clientId: Long) : Config()

        @Parcelize
        data class ClientCreate(val companyId: Long) : Config()

        @Parcelize
        data class ClientAbonementCreate(val companyId: Long, val clientId: Long) : Config()

        @Parcelize
        data class RecordProfile(val recordId: Long) : Config()

    }

}