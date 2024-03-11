package com.angus.common.domain.usecase

import com.angus.common.domain.getway.IUserLocalGateway

interface ILogoutUserUseCase {

    suspend fun logoutUser()

}

class LogoutUserUseCase(private val userLocalGateway: IUserLocalGateway) : ILogoutUserUseCase {

    override suspend fun logoutUser() {
        userLocalGateway.clearConfiguration()
    }

}