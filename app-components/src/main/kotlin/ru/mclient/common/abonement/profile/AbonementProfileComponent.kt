package ru.mclient.common.abonement.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.profile.AbonementProfileStore

class AbonementProfileComponent(
    componentContext: DIComponentContext,
    abonementId: Long,
) : AbonementProfile, DIComponentContext by componentContext {

    private val store: AbonementProfileStore =
        getParameterizedStore { AbonementProfileStore.Params(abonementId) }

    override val state: AbonementProfileState by store.states(this) { it.toState() }

    private fun AbonementProfileStore.State.toState(): AbonementProfileState {
        return AbonementProfileState(
            abonement = abonement?.toState(),
            isRefreshing = isRefreshing,
            isLoading = isLoading
        )
    }

    private fun AbonementProfileStore.State.Abonement.toState(): AbonementProfileState.Abonement {
        return AbonementProfileState.Abonement(
            title = title,
            subabonements = subabonements.map {
                AbonementProfileState.Subabonement(
                    it.title,
                    it.usages,
                )
            },
            services = services.map {
                AbonementProfileState.Service(
                    it.title,
                    it.cost,
                )
            }
        )

    }

    override fun onRefresh() {
        store.accept(AbonementProfileStore.Intent.Refresh)
    }
}
