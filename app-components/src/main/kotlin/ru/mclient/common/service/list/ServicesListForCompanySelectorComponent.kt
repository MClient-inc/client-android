package ru.mclient.common.service.list

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.common.DIComponentContext
import ru.mclient.common.diChildStack
import ru.mclient.common.servicecategory.list.ServiceCategoriesListForCompanyComponent
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateServicesSelectorStore

class ServicesListForCompanySelectorComponent(
    componentContext: DIComponentContext,
    private val companyId: Long,
) : ServicesListSelector, DIComponentContext by componentContext {

    private val store: RecordCreateServicesSelectorStore =
        getSavedStateStore("services_list_selector")

    private val navigation = StackNavigation<Config>()

    override val state: ServicesListSelectorState by store.states(this) { it.toState() }

    var backOnNextExpand = false

    private fun RecordCreateServicesSelectorStore.State.toState(): ServicesListSelectorState {
        if (isExpanded && backOnNextExpand)
            navigation.replaceAll(Config.Categories)
        if (!isExpanded)
            backOnNextExpand = true
        return ServicesListSelectorState(
            isExpanded = isExpanded,
            isAvailable = isAvailable,
            selectedServices = services.map {
                ServicesListSelectorState.Service(
                    id = it.id,
                    title = it.title,
                    cost = it.cost,
                    formattedCost = it.formattedCost,
                    uniqueId = it.uniqueId,
                )
            }
        )
    }

    override val childState: Value<ChildStack<*, ServicesListSelector.Child>> =
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

    override fun onDelete(serviceId: Int) {
        store.accept(RecordCreateServicesSelectorStore.Intent.DeleteById(serviceId))
    }

    private fun onCategorySelected(categoryId: Long) {
        navigation.push(Config.Services(categoryId))
    }

    private fun onServiceSelected(id: Long, title: String, cost: Long, formattedCost: String) {
        store.accept(
            RecordCreateServicesSelectorStore.Intent.Select(
                id = id,
                title = title,
                cost = cost,
                formattedCost = formattedCost
            )
        )
    }

    private fun createChild(
        config: Config,
        componentContext: DIComponentContext,
    ): ServicesListSelector.Child {
        return when (config) {
            is Config.Categories -> ServicesListSelector.Child.CategoryList(
                ServiceCategoriesListForCompanyComponent(
                    componentContext,
                    companyId = companyId,
                    onCategorySelected = this::onCategorySelected,
                )
            )

            is Config.Services -> ServicesListSelector.Child.ServicesList(
                ServiceListForCategoryAndCompanyComponent(
                    componentContext = componentContext,
                    companyId = companyId,
                    categoryId = config.categoryId,
                    onSelect = { onServiceSelected(it.id, it.title, it.cost, it.formattedCost) },
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