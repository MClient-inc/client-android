package ru.mclient.startup.oauth

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)