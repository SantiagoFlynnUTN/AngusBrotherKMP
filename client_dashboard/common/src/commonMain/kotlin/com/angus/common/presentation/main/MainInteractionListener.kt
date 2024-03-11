package com.angus.common.presentation.main

import com.angus.common.presentation.base.BaseInteractionListener

interface MainInteractionListener : BaseInteractionListener {

    fun onClickDropDownMenu()
    fun onDismissDropDownMenu()
    fun onClickLogout()
    fun onSwitchTheme()
}