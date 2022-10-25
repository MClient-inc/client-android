package ru.mclient.common.service.profile

import ru.mclient.common.bar.TopBarHost

interface ServiceProfileHost : TopBarHost {

    val profile: ServiceProfile

}