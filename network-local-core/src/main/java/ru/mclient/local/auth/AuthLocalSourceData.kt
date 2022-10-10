package ru.mclient.local.auth

data class AuthLocalStorageData(
    val accessToken: String,
    val refreshToken: String,
)