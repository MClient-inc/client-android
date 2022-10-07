package ru.mclient.network.auth

data class RefreshTokenInput(
    val refreshToken: String,
)

data class RefreshTokenOutput(
    val accessToken: String,
    val refreshToken: String,
)

data class GetTokenFromCodeInput(
    val code: String,
    val codeVerifier: String,
)

data class GetTokenFromCodeOutput(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)