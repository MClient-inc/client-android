package ru.mclient.startup

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.local.auth.AuthLocalStorageData
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.network.auth.RefreshTokenInput
import ru.shafran.startup.BuildConfig

@Suppress("FunctionName")
fun AuthorizedHttpClient(
    networkSource: AuthNetworkSource,
    localSource: AuthLocalSource
): HttpClient {
    return HttpClient(CIO) {
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
                                val token = localSource.saveTokens(it.accessToken, it.refreshToken)
                                token.toBearer()
                            }
                    } catch (e: Exception) {
                        null
                    }
                }
            }
        }
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger =
                MessageLengthLimitingLogger(delegate = AndroidLogger())
            level = LogLevel.ALL
        }
    }
}

private fun AuthLocalStorageData.toBearer(): BearerTokens {
    return BearerTokens(accessToken, refreshToken)
}

@Suppress("FunctionName")
fun UnauthorizedHttpClient(): HttpClient {
    return HttpClient(CIO) {
        Auth {
            basic {
                realm = null
                credentials {
                    BasicAuthCredentials(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
                }
            }
        }
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            this.logger =
                MessageLengthLimitingLogger(delegate = AndroidLogger())
            this.level = LogLevel.ALL
        }
        followRedirects = false
    }
}

val httpClientModule = module {
    singleOf(::UnauthorizedHttpClient, options = { named("unauthorized") })
    singleOf(::AuthorizedHttpClient, options = { named("authorized") })
}

class AndroidLogger() : Logger {
    override fun log(message: String) {
        Log.d("NetworkHttpClient", message)
    }
}