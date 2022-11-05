package ru.mclient.common.client.list

import ru.mclient.common.bar.MergedHost

interface ClientsListHost : MergedHost {

    val list: ClientsList

}