package ru.mclient.common.service.list

import ru.mclient.common.bar.MergedHost

interface ServiceListHost : MergedHost {

    val list: ServiceList

}