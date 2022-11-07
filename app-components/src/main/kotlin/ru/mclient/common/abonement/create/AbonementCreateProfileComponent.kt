package ru.mclient.common.abonement.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.create.AbonementCreateProfileStore

class AbonementCreateProfileComponent(
    componentContext: DIComponentContext,
) : AbonementCreateProfile, DIComponentContext by componentContext {

    private val store: AbonementCreateProfileStore = getSavedStateStore("abonement_create_profile")

    override val state: AbonementCreateProfileState by store.states(this) { it.toState() }

    private fun AbonementCreateProfileStore.State.toState(): AbonementCreateProfileState {
        return AbonementCreateProfileState(
            title = title,
            isSuccess = isSuccess
        )
    }

    override fun onUpdate(title: String) {
        store.accept(AbonementCreateProfileStore.Intent.Update(title))
    }

}