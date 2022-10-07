package ru.mclient.local.auth

interface AuthLocalSource {

    suspend fun getTokens(): AuthLocalStorageData?

    suspend fun saveTokens(accessToken: String, refreshToken: String, idToken: String)

}