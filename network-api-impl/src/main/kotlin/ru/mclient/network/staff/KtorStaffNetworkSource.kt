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
                    role = it.role
                )
            }
        )
    }

    override suspend fun getStaffById(input: GetStaffByIdInput): GetStaffByIdOutput {
        val response = client.get("/staff/${input.staffId}")
        val body = response.body<GetStaffResponse>()
        return GetStaffByIdOutput(
            id = body.id,
            name = body.name,
            codename = body.codename,
            role = body.role,
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


@Serializable
class GetStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)