package ru.mclient.startup

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.local.auth.AuthLocalStorageData
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.network.auth.RefreshTokenInput

class AuthorizationInterceptor(
    private val networkSource: AuthNetworkSource,
    private val localSource: AuthLocalSource,
) : HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain,
    ): HttpResponse {
        var token: AuthLocalStorageData = mutex.withLock {
            localSource.getTokens()
        } ?: TODO("unauthorized")

        val response =
            chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer ${token.accessToken}")
                    .build()
            )

        return if (response.statusCode == 401) {
            token = mutex.withLock {
                val (access, refresh) = networkSource.refreshToken(RefreshTokenInput(token.refreshToken))
                localSource.saveTokens(access, refresh)
            }
            chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer  ${token.accessToken}")
                    .build()
            )
        } else {
            response
        }
    }
}