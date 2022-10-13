package ru.mclient.network.staff

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class KtorStaffNetworkSource(
    @Named("authorized")
    val client: HttpClient,
) : StaffNetworkSource {

    override suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/staff")
        val body = response.body<GetStaffForCompanyResponse>()
        return GetStaffForCompanyOutput(
            body.staff.map {
                GetStaffForCompanyOutput.Staff(
                    id = it.id,
                    name = it.name,
                    codename = it.codename,
                    icon = it.role
                )
            }
        )
    }
}


@Serializable
class GetStaffForCompanyResponse(
    val staff: List<Staff>,
) {
    @Serializable
    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )
}