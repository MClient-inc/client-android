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
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.network.auth.RefreshTokenInput
import ru.shafran.startup.BuildConfig

val httpClientModule = module {
    single(qualifier = named("unauthorized")) {
        HttpClient(CIO) {
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
        }
    } bind HttpClient::class
    single(qualifier = named("authorized")) {
        val authNetworkSource = get<AuthNetworkSource>()
        val authLocalSource = get<AuthLocalSource>()
        HttpClient(CIO) {
            defaultRequest {
                url(BuildConfig.REST_URI)
            }
            Auth {
                bearer {
                    realm = null
                    loadTokens {
                        val tokens = authLocalSource.getTokens()
                        tokens?.let { BearerTokens(it.accessToken, it.refreshToken) }
                    }
                    refreshTokens {
                        val tokens = oldTokens ?: return@refreshTokens null
                        authNetworkSource.refreshToken(RefreshTokenInput(tokens.refreshToken)).let {
                            BearerTokens(it.accessToken, it.refreshToken)
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
    } bind HttpClient::class
}

class AndroidLogger() : Logger {
    override fun log(message: String) {
        Log.d("NetowrkHttpClient", message)
    }
}