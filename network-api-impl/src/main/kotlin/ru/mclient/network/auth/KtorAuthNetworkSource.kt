package ru.mclient.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
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

//grant_type=authorization_code&code=55mujVN68nBsRnJKflGlHxMBuj7T0_TR9Eiod3dsrgmSNpAIq35ic8GMeKsbBPC9_-4oafcGHH8c9JDQXRV0hZrk3-8-R-pAeIeP9n9hIfXt3ZM55ujsd52TXesDPbux&redirect_uri=https%3A%2F%2Foauthdebugger.com%2Fdebug&state=openid&code_verifier=MeBnSjK0NR-Wy6Kc9mFxG1VMviCcuYWe_EhkQG8llTE&client_id=messaging-client&client_secret=secret

@Single
class KtorAuthNetworkSource(
    @Named("unauthorized")
    private val client: HttpClient
) : AuthNetworkSource {

    override suspend fun refreshToken(input: RefreshTokenInput): RefreshTokenOutput {
        val response =
            client.submitForm(BuildConfig.REFRESH_URI, formParameters = Parameters.build {
                append("grant_type", "refresh_token")
                append("token", input.refreshToken)
                append("token_type_hint ", "refresh_token ")
                append("client_id", BuildConfig.CLIENT_ID)
                append("client_secret", BuildConfig.CLIENT_SECRET)
            }) {
                header("Issuer", BuildConfig.AUTH_URI)
            }
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