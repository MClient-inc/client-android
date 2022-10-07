package ru.mclient.network.account

interface AccountNetworkSource {

    suspend fun getBaseCurrentProfileInfo(): GetBaseCurrentProfileInfoOutput

}