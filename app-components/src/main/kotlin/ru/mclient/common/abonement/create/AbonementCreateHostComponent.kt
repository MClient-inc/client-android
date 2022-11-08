package ru.mclient.common.abonement.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.service.list.ServicesListForCompanySelectorComponent
import ru.mclient.common.service.list.ServicesListSelector
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.create.AbonementCreateStore

class AbonementCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSuccess: (Long) -> Unit,
) : AbonementCreateHost, DIComponentContext by componentContext {

    private val store: AbonementCreateStore =
        getParameterizedStore { AbonementCreateStore.Params(companyId) }

    private val storeState by store.states(this) {
        val abonement = it.abonement
        if (it.isSuccess && abonement != null)
            onSuccess(abonement.id)
        it
    }

    override val state: AbonementCreateHostState
        get() = AbonementCreateHostState(isButtonAvailable = isAvailable())

    private fun isAvailable(): Boolean {
        return profile.state.isSuccess && subabonements.state.isSuccess && storeState.isAvailable
    }

    override val profile: AbonementCreateProfile = AbonementCreateProfileComponent(
        componentContext = childDIContext(key = "abonement_create_profile")
    )

    override val subabonements: AbonementCreateSubabonements =
        AbonementCreateSubabonementsComponent(
            componentContext = childDIContext(key = "abonement_create_subabonements")
        )

    override val services: ServicesListSelector = ServicesListForCompanySelectorComponent(
        componentContext = childDIContext("abonement_create_services"), companyId = companyId
    )

    override fun onContinue() {
        store.accept(
            AbonementCreateStore.Intent.Create(
                title = profile.state.title,
                subabonements = subabonements.state.subabonements.map {
                    AbonementCreateStore.Intent.Create.Subabonement(
                        title = it.title,
                        usages = it.usages,
                        cost = it.cost,
                    )
                },
                services = services.state.selectedServices.map { it.id }
            )
        )
    }

    override val bar: TopBar = ImmutableTopBar(TopBarState("Создание абонемента"))

}