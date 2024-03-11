package com.angus.common.presentation.main

import com.angus.common.presentation.util.ErrorState


data class MainUiState(
    val username: String = "",
    val firstUsernameLetter: String = "",
    val isLogin: Boolean = false,
    val error: ErrorState = ErrorState.UnKnownError,
    val isDropMenuExpanded: Boolean = false,
    val isDarkMode: Boolean = false,
    val hasInternetConnection: Boolean = true
)
