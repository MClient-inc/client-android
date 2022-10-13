package ru.mclient.common.companynetwork.list

import ru.mclient.common.bar.TopBarHost

interface CompanyNetworksSelector : TopBarHost {

    val list: CompanyNetworksList

}