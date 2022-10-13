package ru.mclient.common.company.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class CompaniesSelectorComponent(
    componentContext: DIComponentContext,
    networkId: Long,
    onSelect: (CompaniesListState.Company) -> Unit,
) : CompaniesSelector, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Компании"))

    override val list: CompaniesList =
        CompaniesListForNetworkComponent(
            componentContext = childDIContext(key = "companies_list"),
            networkId = networkId,
            onSelect = onSelect,
        )

}