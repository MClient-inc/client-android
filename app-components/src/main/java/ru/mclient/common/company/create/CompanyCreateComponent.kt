package ru.mclient.common.company.create

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.createCoroutineScope
import ru.mclient.common.utils.getStore
import ru.mclient.mvi.company.create.CompanyCreateStore

class CompanyCreateComponent(
    componentContext: DIComponentContext,
) : CompanyCreate, DIComponentContext by componentContext {

    private val store: CompanyCreateStore = getStore()

    private val componentScope = createCoroutineScope()

    private fun CompanyCreateStore.State.toState(): CompanyCreateState {
        return CompanyCreateState(
            title = title,
            codename = codename,
            description = description,
            isLoading = isLoading,
            isError = isError
        )
    }

    override val state: StateFlow<CompanyCreateState> = store.states.map {
        it.toState()
    }.stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())

    override fun onUpdate(title: String, codename: String, description: String) {
        store.accept(CompanyCreateStore.Intent.Update(title, codename, description))
    }

    override fun onCreate(title: String, codename: String, description: String) {
        store.accept(CompanyCreateStore.Intent.Create(title, codename, description))
    }


}