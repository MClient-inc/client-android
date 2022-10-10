package ru.mclient.local.auth

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single

@Serializable
class DataStoreAuthTokens(
    val accessToken: String,
    val refreshToken: String,
)

@Single
class DataStoreAuthLocalSource(private val context: Context) : AuthLocalSource {

    private val Context.authDataStore by dataStore(
        fileName = "auth",
        serializer = kotlinxDataStoreSerializer<DataStoreAuthTokens>()
    )

    private fun DataStoreAuthTokens.toTokens(): AuthLocalStorageData {
        return AuthLocalStorageData(
            accessToken = accessToken, refreshToken = refreshToken
        )
    }

    override suspend fun getTokens(): AuthLocalStorageData? {
        return context.authDataStore.data.first()?.toTokens()
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.authDataStore.updateData {
            DataStoreAuthTokens(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }
}

