package ru.mclient.network.auth

interface AuthNetworkSource {

    suspend fun refreshToken(input: RefreshTokenInput): RefreshTokenOutput

    suspend fun getTokensFromCode(input: GetTokenFromCodeInput): GetTokenFromCodeOutput

}