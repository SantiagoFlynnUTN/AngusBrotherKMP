package com.angus.common.presentation.login

import com.angus.common.presentation.base.BaseInteractionListener

interface LoginInteractionListener : BaseInteractionListener {

    fun onPasswordChange(password: String)

    fun onUsernameChange(username: String)

    fun onLoginClicked()

    fun onSnackBarDismiss()

}