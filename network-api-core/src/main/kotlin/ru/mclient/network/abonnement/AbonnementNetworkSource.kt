package ru.mclient.network.abonnement

interface AbonnementNetworkSource {

    suspend fun getAbonnementById(input: GetAbonnementByIdInput): GetAbonnementByIdOutput

}