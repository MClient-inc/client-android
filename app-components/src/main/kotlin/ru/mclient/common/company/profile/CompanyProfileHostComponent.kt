package ru.mclient.common.company.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class CompanyProfileHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onStaff: () -> Unit,
    onNetwork: (Long) -> Unit,
    onServices: () -> Unit,
) : CompanyProfileHost, DIComponentContext by componentContext {


    override val bar: TopBar = ImmutableTopBar(
        state = TopBarState("Компания")
    )

    override val profile: CompanyProfile =
        CompanyProfileComponent(
            componentContext = childDIContext(key = "company_profile"),
            companyId = companyId,
            onStaff = onStaff,
            onNetwork = onNetwork,
            onServices = onServices,
        )
}