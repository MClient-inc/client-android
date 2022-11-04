package ru.mclient.startup

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.local.auth.AuthLocalStorageData
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.network.auth.RefreshTokenInput
import ru.mclient.startup.utils.LocalDateSerializer
import ru.mclient.startup.utils.LocalDateTimeSerializer
import ru.mclient.startup.utils.LocalTimeSerializer
import ru.shafran.startup.BuildConfig
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Module
class HttpClientModule {

    @Single
    @Named("authorized")
    fun bindAuthorizedHttpClient(
        networkSource: AuthNetworkSource,
        localSource: AuthLocalSource,
        json: Json,
    ): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(HttpRequestRetry) {
                retryOnException(maxRetries = 5)
                exponentialDelay()
            }
            defaultRequest {
                url(BuildConfig.REST_URI)
            }
            Auth {
                bearer {
                    realm = null
                    loadTokens {
                        val tokens = localSource.getTokens()
                        tokens?.let { BearerTokens(it.accessToken, it.refreshToken) }
                    }
                    refreshTokens {
                        val tokens = localSource.getTokens() ?: return@refreshTokens null
                        try {
                            networkSource.refreshToken(RefreshTokenInput(tokens.refreshToken))
                                .let {
                                    val token =
                                        localSource.saveTokens(it.accessToken, it.refreshToken)
                                    token.toBearer()
                                }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
            install(ContentNegotiation) {
                json(json = json)
            }
            install(Logging) {
                logger =
                    MessageLengthLimitingLogger(delegate = AndroidLogger())
                level = LogLevel.ALL
            }
        }
    }


    @Suppress("FunctionName")
    @Single
    @Named("unauthorized")
    fun bindUnauthorizedHttpClient(json: Json): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(HttpRequestRetry) {
                retryOnException(maxRetries = 5)
                exponentialDelay()
            }
            Auth {
                basic {
                    realm = null
                    credentials {
                        BasicAuthCredentials(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
                    }
                }
            }
            install(ContentNegotiation) {
                json(json = json)
            }
            install(Logging) {
                this.logger =
                    MessageLengthLimitingLogger(delegate = AndroidLogger())
                this.level = LogLevel.ALL
            }
        }
    }


    @Suppress("FunctionName")
    @Single
    fun bindJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                contextual(LocalDate::class, LocalDateSerializer)
                contextual(LocalTime::class, LocalTimeSerializer)
                contextual(LocalDateTime::class, LocalDateTimeSerializer)
            }
        }
    }

}

class AndroidLogger : Logger {
    override fun log(message: String) {
        Log.d("NetworkHttpClient", message)
    }
}


private fun AuthLocalStorageData.toBearer(): BearerTokens {
    return BearerTokens(accessToken, refreshToken)
}