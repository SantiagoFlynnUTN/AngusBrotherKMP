package com.angus.common.presentation.login

import com.angus.common.presentation.restaurant.ErrorWrapper


data class LoginUIState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isUserError: ErrorWrapper? = null,
    val isPasswordError: ErrorWrapper? = null,
    val isEnable: Boolean = false,
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false
)