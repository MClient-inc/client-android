package ru.mclient.local.auth

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KType
import kotlin.reflect.typeOf

private class KotlinxSerializer<T>(
    type: KType,
    override val defaultValue: T,
    private val format: StringFormat,
) : Serializer<T> {

    @Suppress("UNCHECKED_CAST")
    private val serializer = serializer(type) as KSerializer<T>

    override suspend fun readFrom(input: InputStream): T {
        return try {
            val bytes = input.readBytes()
            if (bytes.isNotEmpty()) {
                format.decodeFromString(serializer, bytes.decodeToString())
            } else {
                defaultValue
            }
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        withContext(Dispatchers.IO) {
            if (t != null) {
                val data = format.encodeToString(serializer, t)
                output.write(data.encodeToByteArray())
            } else {
                output.write(byteArrayOf())
            }
        }
    }

}

fun <T> kotlinxDataStoreSerializer(
    type: KType,
    defaultValue: T,
    format: StringFormat,
): Serializer<T> {
    return KotlinxSerializer(type, defaultValue, format)
}


inline fun <reified T> kotlinxDataStoreSerializer(
    defaultValue: T,
    format: StringFormat = Json.Default,
): Serializer<T> {
    return kotlinxDataStoreSerializer(typeOf<T>(), defaultValue = defaultValue, format = format)
}

inline fun <reified T> kotlinxDataStoreSerializer(
    format: StringFormat = Json.Default,
): Serializer<T?> {
    return kotlinxDataStoreSerializer(defaultValue = null, format)
}