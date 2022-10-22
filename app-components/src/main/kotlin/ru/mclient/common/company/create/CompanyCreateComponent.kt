package ru.mclient.common.company.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.company.create.CompanyCreateStore

class CompanyCreateComponent(
    componentContext: DIComponentContext,
) : CompanyCreate, DIComponentContext by componentContext {

    private val store: CompanyCreateStore = getStore()

    private fun CompanyCreateStore.State.toState(): CompanyCreateState {
        return CompanyCreateState(
            title = title,
            codename = codename,
            description = description,
            isLoading = isLoading,
            isError = isError
        )
    }

    override val state: CompanyCreateState by store.states(this) { it.toState() }

    override fun onUpdate(title: String, codename: String, description: String) {
        store.accept(CompanyCreateStore.Intent.Update(title, codename, description))
    }

    override fun onCreate(title: String, codename: String, description: String) {
        store.accept(CompanyCreateStore.Intent.Create(title, codename, description))
    }


}