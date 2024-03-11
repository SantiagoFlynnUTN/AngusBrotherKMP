package com.angus.common.presentation.main

import com.angus.common.presentation.login.LoginUIEffect

sealed interface MainUiEffect{
    object Logout : MainUiEffect
}
