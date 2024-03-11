package com.angus.common.presentation.users

import androidx.compose.runtime.Composable
import com.angus.common.domain.entity.Country
import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.Permission
import com.angus.common.domain.entity.User
import com.angus.common.presentation.composables.table.Header
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.util.ErrorState

data class UserScreenUiState(
    val hasConnection: Boolean = true,
    val pageInfo: UserPageInfoUiState = UserPageInfoUiState(),
    val specifiedUsers: Int = 10,
    val currentPage: Int = 1,
    val search: String = "",
    val permissionsDialog: PermissionsDialogUiState = PermissionsDialogUiState(),
    val filter: FilterUiState = FilterUiState(),
    val error: ErrorState = ErrorState.UnKnownError,
    val isLoading: Boolean = true,
    val userMenu: String = "",
) {
    val allPermissions: List<PermissionUiState> = listOf(
            PermissionUiState.RESTAURANT_OWNER,
            PermissionUiState.DRIVER,
            PermissionUiState.END_USER,
            PermissionUiState.SUPPORT,
            PermissionUiState.DELIVERY,
            PermissionUiState.ADMIN,
    )
    val tableHeader: List<Header>
        @Composable get() = listOf(
            Header(Resources.Strings.number, 1f),
            Header(Resources.Strings.users, 3f),
            Header(Resources.Strings.username, 3f),
            Header(Resources.Strings.email, 3f),
            Header(Resources.Strings.country, 3f),
            Header(Resources.Strings.permission, 3f),
            Header("", 1f),
        )
    data class UserPageInfoUiState(
        val data: List<UserUiState> = emptyList(),
        val numberOfUsers: Int = 0,
        val totalPages: Int = 0,
    )

    data class UserUiState(
        val userId: String = "",
        val fullName: String = "",
        val username: String = "",
        val email: String = "",
        val country: String = "",
        val permissions: List<PermissionUiState> = emptyList(),
    )

    data class PermissionsDialogUiState(
        val show: Boolean = false,
        val id: String = "",
        val permissions: List<PermissionUiState> = emptyList(),
    )

    data class FilterUiState(
        val show: Boolean = false,
        val selectedPermissions: List<PermissionUiState> = emptyList(),
        val countries: List<CountryUiState> = listOf(
            CountryUiState(Country.ARGENTINA),
        ),
    )

    data class CountryUiState(
        val country: Country = Country.ARGENTINA,
        val isSelected: Boolean = false
    )

    enum class PermissionUiState {
        RESTAURANT_OWNER,
        DRIVER,
        END_USER,
        SUPPORT,
        DELIVERY,
        ADMIN,
    }

    enum class Country {
        ARGENTINA
    }
}


fun User.toUiState(): UserScreenUiState.UserUiState {
    return UserScreenUiState.UserUiState(
        userId = id,
        fullName = fullName,
        username = username,
        email = email,
        country = country,
        permissions = permission.map { permission ->
            when (permission) {
                Permission.RESTAURANT_OWNER -> UserScreenUiState.PermissionUiState.RESTAURANT_OWNER
                Permission.DRIVER -> UserScreenUiState.PermissionUiState.DRIVER
                Permission.END_USER -> UserScreenUiState.PermissionUiState.END_USER
                Permission.SUPPORT -> UserScreenUiState.PermissionUiState.SUPPORT
                Permission.DELIVERY -> UserScreenUiState.PermissionUiState.DELIVERY
                Permission.ADMIN -> UserScreenUiState.PermissionUiState.ADMIN
            }
        },
    )
}

fun List<User>.toUiState(): List<UserScreenUiState.UserUiState> = map { it.toUiState() }

fun DataWrapper<User>.toUiState(): UserScreenUiState.UserPageInfoUiState {
    return UserScreenUiState.UserPageInfoUiState(
        data = result.toUiState(),
        totalPages = totalPages,
        numberOfUsers = numberOfResult
    )
}

fun UserScreenUiState.PermissionUiState.toEntity(): Permission {
    return when (this) {
        UserScreenUiState.PermissionUiState.END_USER -> Permission.END_USER
        UserScreenUiState.PermissionUiState.ADMIN -> Permission.ADMIN
        UserScreenUiState.PermissionUiState.SUPPORT -> Permission.SUPPORT
        UserScreenUiState.PermissionUiState.DELIVERY -> Permission.DELIVERY
        UserScreenUiState.PermissionUiState.RESTAURANT_OWNER -> Permission.RESTAURANT_OWNER
        UserScreenUiState.PermissionUiState.DRIVER -> Permission.DRIVER
    }
}

fun List<UserScreenUiState.PermissionUiState>.toEntity() = map { it.toEntity() }

fun UserScreenUiState.Country.toEntity(): Country {
    return when (this) {
        UserScreenUiState.Country.ARGENTINA -> Country.ARGENTINA
    }
}

fun List<UserScreenUiState.Country>.toCountryEntity() = this.map { it.toEntity() }

data class PermissionInformation(
    val iconPath: String,
    val title: String,
)