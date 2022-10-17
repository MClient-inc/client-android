package ru.mclient.network.staff

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    override suspend fun createStaff(input: CreateStaffInput): CreateStaffOutput {
        val response = client.post("/companies/${input.companyId}/staff") {
            setBody(
                CreateStaffRequest(
                    name = input.name,
                    codename = input.codename,
                    role = input.role,
                ),
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateStaffResponse>()
        return CreateStaffOutput(
            id = body.id,
            name = body.name,
            codename = body.codename,
            role = body.role,
        )
    }
}

@Serializable
class CreateStaffRequest(
    val name: String,
    val codename: String,
    val role: String,
)

@Serializable
class CreateStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)

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