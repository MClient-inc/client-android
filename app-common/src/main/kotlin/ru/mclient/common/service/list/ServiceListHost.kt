package ru.mclient.common.service.list

import ru.mclient.common.bar.TopBarHost

interface ServiceListHost : TopBarHost {

    val list: ServiceList

}