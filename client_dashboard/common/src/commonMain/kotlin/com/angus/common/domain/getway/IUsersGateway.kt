package com.angus.common.domain.getway

import com.angus.common.domain.entity.Country
import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.Permission
import com.angus.common.domain.entity.User

interface IUsersGateway {

    suspend fun getUsers(
        query: String?=null,
        byPermissions: List<Permission>,
        byCountries: List<Country>,
        page: Int,
        numberOfUsers: Int,
    ): DataWrapper<User>

    suspend fun loginUser(username: String, password: String): Pair<String, String>

    suspend fun deleteUser(userId: String): Boolean

    suspend fun getLastRegisteredUsers(limit : Int): List<User>

    suspend fun updateUserPermissions(userId: String, permissions: List<Permission>): User

}