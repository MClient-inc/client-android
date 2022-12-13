package ru.mclient.network.account

import com.apollographql.apollo3.ApolloClient
import org.koin.core.annotation.Single
import ru.mclient.network.data.GetAccountQuery

@Single
class GraphQLAccountNetworkSource(
    private val client: ApolloClient,
) : AccountNetworkSource {

    override suspend fun getBaseCurrentProfileInfo(): GetBaseCurrentProfileInfoOutput {
        val response = client.query(GetAccountQuery()).execute()
        val data = response.dataAssertNoErrors.account.baseAccount
        return GetBaseCurrentProfileInfoOutput(
            id = data.id.toLong(),
            username = data.username,
            name = data.data.name,
            avatar = null,
        )
    }

}