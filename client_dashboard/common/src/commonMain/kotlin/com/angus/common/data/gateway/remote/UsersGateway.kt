package com.angus.common.data.gateway.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import com.angus.common.data.gateway.remote.mapper.mapPermissionsToInt
import com.angus.common.data.gateway.remote.mapper.toEntity
import com.angus.common.data.gateway.remote.model.ServerResponse
import com.angus.common.data.gateway.remote.model.UserDto
import com.angus.common.data.gateway.remote.model.UserResponse
import com.angus.common.data.gateway.remote.model.UserTokensRemoteDto
import com.angus.common.domain.entity.Country
import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.Permission
import com.angus.common.domain.entity.User
import com.angus.common.domain.getway.IUsersGateway

class UsersGateway(private val client: HttpClient) : BaseGateway(), IUsersGateway {

    override suspend fun getUsers(
        query: String?,
        byPermissions: List<Permission>,
        byCountries: List<Country>,
        page: Int,
        numberOfUsers: Int
    ): DataWrapper<User> {
        val result = tryToExecute<ServerResponse<UserResponse>>(client) {
            val permissions = mapPermissionsToInt(byPermissions)
            get(urlString = "/users") {
                parameter("page", page)
                parameter("limit", numberOfUsers)
                parameter("permissions", permissions)
                parameter("countries", byCountries)
                parameter("query", query)
            }
        }.value
        return paginateData(result?.users?.toEntity() ?: emptyList(), numberOfUsers, result?.total ?: 0)
    }

    override suspend fun loginUser(username: String, password: String): Pair<String, String> {
        val result = tryToExecute<ServerResponse<UserTokensRemoteDto>>(client) {
            submitForm(
                formParameters = Parameters.build {
                    append("username", username)
                    append("password", password)
                }
            ) {
                url("/login")
            }
        }.value
        println("accessToken: ${result?.accessToken ?: ""}")
        return Pair(result?.accessToken ?: "", result?.refreshToken ?: "")
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return tryToExecute<ServerResponse<Boolean>>(client) {
            delete(urlString = "/user") { url { appendPathSegments(userId) } }
        }.value ?: false
    }

    override suspend fun getLastRegisteredUsers(limit: Int): List<User> {
        return tryToExecute<ServerResponse<List<UserDto>>>(client) {
            get(urlString = "/user/last-register?limit=4") {
                parameter("limit", limit)
            }
        }.value?.toEntity() ?: throw UnknownError()
    }

    override suspend fun updateUserPermissions(
        userId: String,
        permissions: List<Permission>
    ): User {
        return tryToExecute<ServerResponse<UserDto>>(client) {
            put(urlString = "/user/$userId/permission") {
                setBody(
                    Json.encodeToString(
                        ListSerializer(Int.serializer()),
                        mapPermissionsToInt(permissions)
                    )
                )
            }
        }.value?.toEntity() ?: throw UnknownError()
    }

}