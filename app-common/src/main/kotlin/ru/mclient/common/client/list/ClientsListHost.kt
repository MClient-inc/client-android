package ru.mclient.common.client.list

import ru.mclient.common.bar.TopBarHost

interface ClientsListHost : TopBarHost {

    val list: ClientsList

}