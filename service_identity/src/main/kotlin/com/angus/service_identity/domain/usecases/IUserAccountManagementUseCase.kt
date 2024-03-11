package com.angus.service_identity.domain.usecases

import org.koin.core.annotation.Single
import com.angus.service_identity.domain.entity.User
import com.angus.service_identity.domain.entity.UserInfo
import com.angus.service_identity.domain.entity.UserManagement
import com.angus.service_identity.domain.entity.Wallet
import com.angus.service_identity.domain.gateway.IDataBaseGateway
import com.angus.service_identity.domain.security.HashingService
import com.angus.service_identity.domain.usecases.validation.IUserInfoValidationUseCase
import com.angus.service_identity.domain.usecases.validation.IWalletBalanceValidationUseCase
import com.angus.service_identity.domain.util.*
import com.angus.service_identity.endpoints.model.mapper.toUserDetails

interface IUserAccountManagementUseCase {

    suspend fun createUser(password: String?, user: UserInfo): User

    suspend fun deleteUser(id: String): Boolean

    suspend fun updateUser(id: String, fullName: String? = null, phone: String? = null): User

    suspend fun getUser(id: String): User

    suspend fun login(username: String, password: String, applicationId: String): Boolean

    suspend fun getUserByUsername(username: String): UserManagement

}

@Single
class UserAccountManagementUseCase(
    private val dataBaseGateway: IDataBaseGateway,
    private val userInfoValidationUseCase: IUserInfoValidationUseCase,
    private val hashingService: HashingService
) : IUserAccountManagementUseCase {

    override suspend fun createUser(password: String?, user: UserInfo): User {
        userInfoValidationUseCase.validateUserInformation(password = password, user = user)
        if (password == null) {
            throw RequestValidationException(listOf(INVALID_REQUEST_PARAMETER))
        }
        val saltedHash = hashingService.generateSaltedHash(password)
        val userCountry = getUserCountry(user.phone)
        val newUser = dataBaseGateway.createUser(saltedHash, country = userCountry.name, user = user)
        val wallet = dataBaseGateway.createWallet(newUser.id, currency = userCountry.currency)
        dataBaseGateway.addAddress(newUser.id, user.addresses.first())
        val userDetails = newUser.toUserDetails(wallet.currency, wallet.walletBalance)
        return userDetails
    }

    override suspend fun getUserByUsername(username: String): UserManagement {
        return dataBaseGateway.getUserByUsername(username)
    }

    override suspend fun login(username: String, password: String, applicationId: String): Boolean {
        val saltedHash = dataBaseGateway.getSaltedHash(username)
        return if (hashingService.verify(password, saltedHash)) {
            val userPermission = dataBaseGateway.getUserPermissionByUsername(username)
            if (verifyPermissionToLogin(userPermission, applicationId)) {
                true
            } else {
                throw InvalidCredentialsException(INVALID_PERMISSION)
            }
        } else throw InvalidCredentialsException(INVALID_CREDENTIALS)
    }

    override suspend fun deleteUser(id: String): Boolean {
        if (dataBaseGateway.isUserDeleted(id)) {
            throw ResourceNotFoundException(NOT_FOUND)
        }
        return dataBaseGateway.deleteUser(id)
    }

    override suspend fun updateUser(id: String, fullName: String?, phone: String?): User {
        userInfoValidationUseCase.validateUpdateUserInformation(fullName, phone)
        dataBaseGateway.updateUser(id, fullName, phone)
        return dataBaseGateway.getUserById(id)
    }

    override suspend fun getUser(id: String): User {
        return dataBaseGateway.getUserById(id)
    }

    private fun verifyPermissionToLogin(userPermission: Int, applicationId: String): Boolean {
        val applicationIds = getApplicationIdFromEnvironment()
        return true/*applicationIds.filterValues { pair -> pair.first == applicationId && (pair.second and userPermission) == pair.second }
            .isNotEmpty()*/
    }

    private fun getApplicationIdFromEnvironment(): HashMap<String, Pair<String, Int>> {
        val map = hashMapOf<String, Pair<String, Int>>()
        map[ApplicationId.END_USER] = Pair("client_end_user", Role.END_USER)
        map[ApplicationId.RESTAURANT] = Pair("client_restaurant", Role.RESTAURANT_OWNER)
        map[ApplicationId.DASHBOARD] = Pair("client_dashboard", Role.DASHBOARD_ADMIN)
        map[ApplicationId.TAXI_DRIVER] = Pair("client_taxi_driver".toString(), Role.TAXI_DRIVER)
        map[ApplicationId.DELIVERY] = Pair("client_delivery", Role.DELIVERY)
        map[ApplicationId.SUPPORT] = Pair("client_support", Role.SUPPORT)
        return map
    }

    private fun getUserCountry(phone: String): CountryCurrency {
        val matchingEntry = countryMap.entries.find { phone.startsWith(it.value) }
        val countryName = matchingEntry?.key ?: "Unknown"
        return CountryCurrency.valueOf(countryName)
    }

}