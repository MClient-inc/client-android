package ru.mclient.common.company.list

import ru.mclient.common.bar.TopBarHost

interface CompaniesSelector : TopBarHost {

    val list: CompaniesList

}