package ru.mclient.network.abonnement

import io.ktor.client.HttpClient
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class KtorAbonnementNetworkSource(
    @Named("authorized")
    val client: HttpClient
): AbonnementNetworkSource {



}