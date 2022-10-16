package ru.mclient.startup.oauth

import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import ru.mclient.common.auth.oauth.AuthorizationState
import ru.mclient.common.auth.oauth.OAuthRequest
import ru.mclient.common.utils.CoroutineInstance
import ru.mclient.startup.oauth.AndroidOAuthRequestInstance.Config.CALLBACK_URI
import ru.mclient.startup.oauth.AndroidOAuthRequestInstance.Config.CLIENT_ID
import ru.mclient.startup.oauth.AndroidOAuthRequestInstance.Config.RESPONSE_TYPE
import ru.mclient.startup.oauth.AndroidOAuthRequestInstance.Config.serviceConfiguration
import ru.shafran.startup.BuildConfig

class AndroidOAuthRequestInstance(private val authService: AuthorizationService) :
    OAuthRequest, CoroutineInstance() {

    override val state: MutableSharedFlow<AuthorizationState> =
        MutableSharedFlow()

    override fun getOpenPageIntent(): Intent {
        val tabIntent = CustomTabsIntent.Builder()
            .setShareState(SHARE_STATE_OFF)
            .build()
        val request = buildTokenRequest()
        Log.d("NetworkAuthDebug", "create request with code verifier ${request.codeVerifier}")
        return authService.getAuthorizationRequestIntent(request, tabIntent)
    }

    private fun buildTokenRequest(): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            serviceConfiguration,
            CLIENT_ID,
            RESPONSE_TYPE,
            CALLBACK_URI.toUri()
        ).setScope(Config.SCOPE).build()
    }

//
//suspend fun preformRequest(request: TokenRequest): TokenResponse {
//    return suspendCoroutine {
//        authService.performTokenRequest(
//            request, ClientSecretPost(Config.CLIENT_SECRET)
//        ) { response, exception ->
//            if (exception != null) {
//                it.resumeWithException(exception)
//                return@performTokenRequest
//            }
//            if (response != null) {
//                it.resume(response)
//                return@performTokenRequest
//            }
//            throw IllegalStateException("response and exception cant be null together")
//        }
//    }
//}


//
//    private fun buildRefreshTokenRequest(refreshToken: String): TokenRequest {
//        return TokenRequest.Builder(
//            serviceConfiguration,
//            CLIENT_ID,
//        ).setScope(Config.SCOPE)
//            .setRefreshToken(refreshToken)
//            .setGrantType(GrantTypeValues.REFRESH_TOKEN).build()
//    }

    override fun onAuthorizationCompleted(data: Intent) {
        AuthorizationException.fromIntent(data)?.let(::onAuthorizationException)
        AuthorizationResponse.fromIntent(data)?.let(::onAuthorizationResponse)
    }

    private fun onAuthorizationException(response: AuthorizationException) {
        Log.w("MclientOAuth", "exception during authorization", response)
        scope.launch {
            state.emit(AuthorizationState.Failure)
        }
    }

    private fun onAuthorizationResponse(response: AuthorizationResponse) {
        scope.launch {
            val verifier = response.request.codeVerifier
            val code = response.authorizationCode
            if (code != null && verifier != null) {
                state.emit(AuthorizationState.Success(code, verifier))
            } else {
                state.emit(AuthorizationState.Failure)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }

    private object Config {


        const val AUTH_URI = BuildConfig.AUTH_URI
        const val TOKEN_URI = BuildConfig.TOKEN_URI
        const val CLIENT_ID = BuildConfig.CLIENT_ID
        const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
        const val CALLBACK_URI = BuildConfig.CALLBACK_URI
        const val SCOPE = "openid"

        const val RESPONSE_TYPE = ResponseTypeValues.CODE

        val serviceConfiguration = AuthorizationServiceConfiguration(
            AUTH_URI.toUri(),
            TOKEN_URI.toUri(),
        )
    }

}