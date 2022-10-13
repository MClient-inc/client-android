package ru.mclient.common.companynetwork.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class CompanyNetworksSelectorComponent(
    componentContext: DIComponentContext,
    accountId: Long,
    onSelect: (CompanyNetworksListState.CompanyNetwork) -> Unit,
) : CompanyNetworksSelector, DIComponentContext by componentContext {
    override val list: CompanyNetworksList = CompanyNetworksForAccountComponent(
        componentContext = childDIContext(key = "company_networks_list"),
        accountId = accountId,
        onSelect = onSelect
    )
    override val bar: TopBar = ImmutableTopBar(state = TopBarState(title = "Мои сети"))
}