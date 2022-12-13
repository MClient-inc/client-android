package ru.mclient.startup

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.adapter.KotlinxLocalDateAdapter
import com.apollographql.apollo3.adapter.KotlinxLocalDateTimeAdapter
import com.apollographql.apollo3.api.CustomScalarType
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.startup.utils.KotlinxLocalTimeAdapter
import ru.shafran.startup.BuildConfig

@Module
class GraphQLClientModule {

    @Single
    fun bindClient(
        networkSource: AuthNetworkSource,
        localSource: AuthLocalSource,
    ): ApolloClient {
        return ApolloClient.Builder()
            .addCustomScalarAdapter(
                CustomScalarType("LocalDate", "kotlinx.datetime.LocalDate"),
                KotlinxLocalDateAdapter
            )
            .addCustomScalarAdapter(
                CustomScalarType("LocalTime", "kotlinx.datetime.LocalTime"),
                KotlinxLocalTimeAdapter,
            )
            .addCustomScalarAdapter(
                CustomScalarType("LocalDateTime", "kotlinx.datetime.LocalDateTime"),
                KotlinxLocalDateTimeAdapter,
            )
            .serverUrl(BuildConfig.GRAPHQL_URI)
            .addHttpInterceptor(AuthorizationInterceptor(networkSource, localSource))
            .build()
    }


}