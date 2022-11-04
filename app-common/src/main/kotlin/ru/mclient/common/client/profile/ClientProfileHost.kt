package ru.mclient.common.client.profile

import ru.mclient.common.bar.TopBarHost

interface ClientProfileHost: TopBarHost {

    val profile: ClientProfile

}