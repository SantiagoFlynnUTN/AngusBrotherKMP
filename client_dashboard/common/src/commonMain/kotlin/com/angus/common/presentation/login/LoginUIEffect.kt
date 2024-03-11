package com.angus.common.presentation.login

sealed interface LoginUIEffect {

    object LoginSuccess : LoginUIEffect

    class LoginFailed(val errorMessage: String) : LoginUIEffect

}