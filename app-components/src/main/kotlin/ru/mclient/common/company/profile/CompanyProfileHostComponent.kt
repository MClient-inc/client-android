package ru.mclient.common.company.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class CompanyProfileHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : CompanyProfileHost, DIComponentContext by componentContext {


    override val bar: TopBar = ImmutableTopBar(
        state = TopBarState("Компания")
    )

    override val profile: CompanyProfile = CompanyProfileComponent(childDIContext("company_profile"), companyId)
}