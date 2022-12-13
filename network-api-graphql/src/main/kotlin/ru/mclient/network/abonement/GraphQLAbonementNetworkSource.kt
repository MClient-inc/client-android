package ru.mclient.network.abonement

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.core.annotation.Single
import ru.mclient.network.data.AddAbonementMutation
import ru.mclient.network.data.AddClientAbonementMutation
import ru.mclient.network.data.GetAbonementQuery
import ru.mclient.network.data.GetAbonementsForClientQuery
import ru.mclient.network.data.GetAbonementsForCompanyQuery
import ru.mclient.network.data.type.AddAbonementInput
import ru.mclient.network.data.type.AddAbonementSubabonementInput


@Single
class GraphQLAbonementNetworkSource(
    private val client: ApolloClient,
) : AbonementNetworkSource {

    override suspend fun getAbonementById(input: GetAbonementByIdInput): GetAbonementByIdOutput {
        val response = client.query(GetAbonementQuery(input.abonementId)).execute()
        val data = response.dataAssertNoErrors.abonement
        return GetAbonementByIdOutput(
            GetAbonementByIdOutput.Abonement(
                id = data.id,
                title = data.data.title,
                services = data.services.map {
                    GetAbonementByIdOutput.Service(
                        id = it.id,
                        title = it.data.title,
                        cost = it.data.cost.basicCost.rawCost,
                    )
                },
                subabonements = data.subabonements.map {
                    GetAbonementByIdOutput.Subabonement(
                        id = it.id,
                        title = it.data.title,
                        usages = it.info.usages,
                        liveTimeInMillis = it.info.liveInMillis,
                        availableUntil = it.info.availableUntil.toJavaLocalDateTime(),
                    )
                }
            )
        )
    }

    override suspend fun getAbonementsForCompany(input: GetAbonementsForCompanyInput): GetAbonementsForCompanyOutput {
        val response = client.query(GetAbonementsForCompanyQuery(input.companyId)).execute()
        return GetAbonementsForCompanyOutput(
            response.dataAssertNoErrors.company.abonements.map { abonement ->
                GetAbonementsForCompanyOutput.Abonement(
                    id = abonement.id,
                    title = abonement.data.title,
                    subabonements = abonement.subabonements.map {
                        GetAbonementsForCompanyOutput.Subabonement(
                            id = it.id,
                            title = it.data.title,
                            cost = it.data.cost.basicCost.rawCost,
                            usages = it.info.usages,
                            liveTimeInMillis = it.info.liveInMillis,
                            availableUntil = it.info.availableUntil.toJavaLocalDateTime(),
                        )
                    }
                )
            }
        )
    }

    override suspend fun createAbonement(input: CreateAbonementInput): CreateAbonementOutput {
        val response = client.mutation(
            AddAbonementMutation(
                input.companyId,
                AddAbonementInput(
                    title = input.title,
                    services = Optional.present(input.services),
                    subabonements = Optional.present(input.subabonements.map {
                        AddAbonementSubabonementInput(
                            title = it.title,
                            cost = it.cost,
                            usages = it.usages,
                        )
                    })
                )
            )
        ).execute()
        val data = response.dataAssertNoErrors.company.addAbonement
        return CreateAbonementOutput(
            abonement = CreateAbonementOutput.Abonement(
                id = data.id,
                title = data.data.title,
                services = data.services.map {
                    CreateAbonementOutput.Service(
                        id = it.id,
                        title = it.data.title,
                        cost = it.data.cost.basicCost.rawCost,
                    )
                },
                subabonements = data.subabonements.map {
                    CreateAbonementOutput.Subabonement(
                        id = it.id,
                        title = it.data.title,
                        cost = it.data.cost.rawCost,
                        usages = it.info.usages,
                        liveTimeInMillis = it.info.liveInMillis,
                        availableUntil = it.info.availableUntil.toJavaLocalDateTime(),
                    )
                }
            )
        )
    }

    override suspend fun getAbonementsForClient(input: GetAbonementsForClientInput): GetAbonementsForClientOutput {
        val response = client.query(GetAbonementsForClientQuery(input.clientId)).execute()
        return GetAbonementsForClientOutput(response.dataAssertNoErrors.client.abonements.map {
            GetAbonementsForClientOutput.ClientAbonement(
                id = it.id,
                usages = it.data.currentUsages,
                abonement = GetAbonementsForClientOutput.Abonement(
                    id = it.subabonement.abonement.id,
                    title = it.subabonement.abonement.data.title,
                    subabonement = GetAbonementsForClientOutput.Subabonement(
                        id = it.subabonement.id,
                        title = it.subabonement.data.title,
                        cost = it.subabonement.data.cost.basicCost.rawCost,
                        maxUsages = it.subabonement.info.usages,
                    )
                )
            )
        })
    }

    override suspend fun addAbonementForClient(input: AddAbonementToClientInput): AddAbonementToClientOutput {
        val response = client.mutation(
            AddClientAbonementMutation(
                input.clientId,
                input.subabonementId,
            )
        ).execute()
        val data = response.dataAssertNoErrors.client.addClientAbonement
        return AddAbonementToClientOutput(
            clientId = data.client.id,
        )
    }
}