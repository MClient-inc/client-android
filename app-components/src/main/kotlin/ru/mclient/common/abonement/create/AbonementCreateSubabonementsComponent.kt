package ru.mclient.common.abonement.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.create.AbonementCreateSubabonementsStore

class AbonementCreateSubabonementsComponent(
    componentContext: DIComponentContext,
) : AbonementCreateSubabonements, DIComponentContext by componentContext {

    private val store: AbonementCreateSubabonementsStore =
        getSavedStateStore("abonement_create_subabonements")

    override val state: AbonementCreateSubabonementsState by store.states(this) { it.toState() }

    private fun AbonementCreateSubabonementsStore.State.toState(): AbonementCreateSubabonementsState {
        return AbonementCreateSubabonementsState(
            isSuccess = isSuccess,
            creation = AbonementCreateSubabonementsState.Creation(
                title = creation.title,
                usages = creation.usages,
                isAvailable = creation.isAvailable,
                isButtonAvailable = creation.isContinueAvailable,
            ),
            subabonements = subabonements.map {
                AbonementCreateSubabonementsState.Subabonement(
                    it.title,
                    it.usages,
                    it.uniqueId,
                )
            }
        )

    }

    override fun onCreate() {
        store.accept(AbonementCreateSubabonementsStore.Intent.Create)
    }

    override fun onUpdate(title: String, usages: String) {
        store.accept(
            AbonementCreateSubabonementsStore.Intent.Update(
                title,
                usages.toIntOrNull() ?: 0
            )
        )
    }

    override fun onDelete(uniqueId: Int) {
        store.accept(AbonementCreateSubabonementsStore.Intent.DeleteById(uniqueId))
    }

}
