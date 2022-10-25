package ru.mclient.network.client

class GetClientsForCompanyInput(val companyId: Long)
class GetClientsForCompanyOutput(
    val clients: List<Client>
) {
    class Client(
        val id: Long,
        val title: String,
        val phone: String,
    )
}
