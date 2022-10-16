package ru.mclient.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import ru.shafran.network.BuildConfig

@Serializable
class GetTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("id_token")
    val idToken: String,
)

@Serializable
class GetTokenRefreshResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Single
class KtorAuthNetworkSource(
    @Named("unauthorized")
    private val client: HttpClient
) : AuthNetworkSource {

    override suspend fun refreshToken(input: RefreshTokenInput): RefreshTokenOutput {
        val response =
            client.submitForm(BuildConfig.TOKEN_URI, formParameters = Parameters.build {
                append("grant_type", "refresh_token")
                append("refresh_token", input.refreshToken)
                append("client_id", BuildConfig.CLIENT_ID)
                append("client_secret", BuildConfig.CLIENT_SECRET)
                append("scope", BuildConfig.SCOPE)
            })
        val body = response.body<GetTokenRefreshResponse>()
        return RefreshTokenOutput(
            accessToken = body.accessToken,
            refreshToken = body.refreshToken,
        )
    }

    override suspend fun getTokensFromCode(input: GetTokenFromCodeInput): GetTokenFromCodeOutput {
        val response = client.submitForm(BuildConfig.TOKEN_URI, formParameters = Parameters.build {
            append("grant_type", "authorization_code")
            append("code", input.code)
            append("redirect_uri", BuildConfig.CALLBACK_URI)
            append("code_verifier", input.codeVerifier)
            append("client_id", BuildConfig.CLIENT_ID)
            append("client_secret", BuildConfig.CLIENT_SECRET)
        })
        val body = response.body<GetTokenResponse>()
        return GetTokenFromCodeOutput(
            accessToken = body.accessToken,
            refreshToken = body.refreshToken,
            idToken = body.idToken,
        )
    }
}