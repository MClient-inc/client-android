package ru.mclient.network.client

class GetClientsForCompanyInput(val companyId: Long)
class GetClientsForCompanyOutput(
    val clients: List<Client>,
) {
    class Client(
        val id: Long,
        val title: String,
        val phone: String,
    )
}

data class GetClientByIdInput(
    val clientId: Long,
)

data class GetClientByIdOutput(
    val id: Long,
    val name: String,
    val phone: String,
)

class CreateClientInput(
    val companyId: Long,
    val name: String,
    val phone: String,
)

class CreateClientOutput(
    val id: Long,
    val name: String,
    val phone: String,
)
