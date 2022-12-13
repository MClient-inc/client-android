package ru.mclient.common.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.abonement.clientcreate.AbonementClientCreateHost
import ru.mclient.common.abonement.create.AbonementCreateHost
import ru.mclient.common.abonement.list.AbonementsListHost
import ru.mclient.common.abonement.profile.AbonementProfileHost
import ru.mclient.common.client.create.ClientCreateHost
import ru.mclient.common.client.list.ClientsListHost
import ru.mclient.common.client.profile.ClientProfileHost
import ru.mclient.common.company.profile.CompanyProfileHost
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileHost
import ru.mclient.common.home.block.HomeBlockHost
import ru.mclient.common.record.create.RecordCreateHost
import ru.mclient.common.record.list.RecordsListHost
import ru.mclient.common.record.profile.RecordProfileHost
import ru.mclient.common.service.create.ServiceCreateHost
import ru.mclient.common.service.list.ServiceListHost
import ru.mclient.common.service.profile.ServiceProfileHost
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateHost
import ru.mclient.common.servicecategory.list.ServiceCategoriesListHost
import ru.mclient.common.staff.create.StaffCreateHost
import ru.mclient.common.staff.list.StaffListHost
import ru.mclient.common.staff.profile.StaffProfileHost
import ru.mclient.common.staff.schedule.StaffScheduleHost

interface MultipleStackHost {

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {

        @JvmInline
        value class CompanyProfile(val component: CompanyProfileHost) : Child


        @JvmInline
        value class StaffList(val component: StaffListHost) : Child


        @JvmInline
        value class StaffProfile(val component: StaffProfileHost) : Child


        @JvmInline
        value class StaffCreate(val component: StaffCreateHost) : Child


        @JvmInline
        value class CompanyNetwork(val component: CompanyNetworkProfileHost) : Child

        @JvmInline
        value class ServiceCategoriesList(val component: ServiceCategoriesListHost) : Child

        @JvmInline
        value class ServiceCategoryCreate(val component: ServiceCategoryCreateHost) : Child

        @JvmInline
        value class ServiceList(val component: ServiceListHost) : Child

        @JvmInline
        value class ServiceCreate(val component: ServiceCreateHost) : Child

        @JvmInline
        value class ClientsList(val component: ClientsListHost) : Child

        @JvmInline
        value class ClientProfile(val component: ClientProfileHost) : Child

        @JvmInline
        value class ClientCreate(val component: ClientCreateHost) : Child

        @JvmInline
        value class ClientAbonementCreate(val component: AbonementClientCreateHost) : Child

        @JvmInline
        value class ServiceProfile(val component: ServiceProfileHost) : Child

        @JvmInline
        value class StaffSchedule(val component: StaffScheduleHost) : Child

        @JvmInline
        value class RecordProfile(val component: RecordProfileHost) : Child

        @JvmInline
        value class HomeBlock(val component: HomeBlockHost) : Child

        @JvmInline
        value class RecordsList(val component: RecordsListHost) : Child

        @JvmInline
        value class RecordCreate(val component: RecordCreateHost) : Child

        @JvmInline
        value class AbonementsList(val component: AbonementsListHost) : Child

        @JvmInline
        value class AbonementsProfile(val component: AbonementProfileHost) : Child

        @JvmInline
        value class AbonementCreate(val component: AbonementCreateHost) : Child

    }


}