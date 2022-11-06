package ru.mclient.common.record.create

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.diChildStack
import ru.mclient.common.service.list.ServiceListForCategoryAndCompanyComponent
import ru.mclient.common.servicecategory.list.ServiceCategoriesListForCompanyComponent
import ru.mclient.common.utils.getStoreSavedState
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateServicesSelectorStore

class RecordCreateServicesSelectorComponent(
    componentContext: DIComponentContext,
    private val companyId: Long,
) : RecordCreateServicesSelector, DIComponentContext by componentContext {

    private val store: RecordCreateServicesSelectorStore =
        getStoreSavedState("record_create_services")

    private val navigation = StackNavigation<Config>()

    override val state: RecordCreateServicesSelectorState by store.states(this) { it.toState() }

    private fun RecordCreateServicesSelectorStore.State.toState(): RecordCreateServicesSelectorState {
        return RecordCreateServicesSelectorState(
            isExpanded = isExpanded, isAvailable = isAvailable,
            selectedServices = services.map {
                RecordCreateServicesSelectorState.Service(
                    it.id,
                    it.title,
                    it.cost,
                    it.formattedCost,
                )
            }
        )
    }

    override val childState: Value<ChildStack<*, RecordCreateServicesSelector.Child>> =
        diChildStack(
            source = navigation,
            initialConfiguration = Config.Categories,
            childFactory = this::createChild,
        )

    override fun onDismiss() {
        store.accept(RecordCreateServicesSelectorStore.Intent.Move(false))
    }

    override fun onExpand() {
        store.accept(RecordCreateServicesSelectorStore.Intent.Move(true))
    }

    override fun onDelete(serviceId: Long) {
        store.accept(RecordCreateServicesSelectorStore.Intent.DeleteById(serviceId))
    }

    private fun onCategorySelected(categoryId: Long) {
        navigation.push(Config.Services(categoryId))
    }

    private fun onServiceSelected(id: Long, title: String, cost: Long) {
        store.accept(RecordCreateServicesSelectorStore.Intent.Select(id, title, cost))
    }

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext,
    ): RecordCreateServicesSelector.Child {
        return when (config) {
            is Config.Categories -> RecordCreateServicesSelector.Child.CategoryList(
                ServiceCategoriesListForCompanyComponent(
                    componentContext,
                    companyId = companyId,
                    onCategorySelected = this::onCategorySelected,
                )
            )

            is Config.Services -> RecordCreateServicesSelector.Child.ServicesList(
                ServiceListForCategoryAndCompanyComponent(
                    componentContext = componentContext,
                    companyId = companyId,
                    categoryId = config.categoryId,
                    onSelect = { onServiceSelected(it.id, it.title, it.cost) },
                )
            )
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        object Categories : Config

        @Parcelize
        @JvmInline
        value class Services(val categoryId: Long) : Config

    }

}