package com.angus.common.presentation.main

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.angus.common.domain.usecase.IExploreDashboardUseCase
import com.angus.common.domain.usecase.ILogoutUserUseCase
import com.angus.common.domain.usecase.IManageThemeUseCase
import com.angus.common.presentation.base.BaseScreenModel
import com.angus.common.presentation.util.ErrorState


class MainScreenModel(
    private val exploreDashboard: IExploreDashboardUseCase,
    private val logout: ILogoutUserUseCase,
    private val themeManagement: IManageThemeUseCase
) : BaseScreenModel<MainUiState, MainUiEffect>(MainUiState()), MainInteractionListener {


    init {
        getUserInfo()
        getCurrentThemeMode()
    }

    private fun getUserInfo() {
        tryToExecute(
            exploreDashboard::getUserInfo,
            ::onGetUserInfoSuccessfully,
            ::onError
        )
    }

    private fun onGetUserInfoSuccessfully(username: String) {
        updateState {
            it.copy(
                username = username,
                firstUsernameLetter = username.first().uppercase(),
                hasInternetConnection = true
            )
        }
    }

    private fun onError(error: ErrorState) {
        when(error){
            is ErrorState.NoConnection -> {
                updateState { it.copy(error = error, hasInternetConnection = true) }
            }
            else -> {
                updateState { it.copy(error = error) }
            }
        }
    }

    override fun onClickDropDownMenu() {
        updateState { it.copy(isDropMenuExpanded = true) }
    }

    override fun onDismissDropDownMenu() {
        updateState { it.copy(isDropMenuExpanded = false) }
    }

    override fun onClickLogout() {
        tryToExecute(
            logout::logoutUser,
            { onLogoutSuccessfully() },
            ::onError
        )
    }

    private fun onLogoutSuccessfully() {
        updateState { it.copy(isLogin = false) }
        sendNewEffect(MainUiEffect.Logout)
    }

    override fun onSwitchTheme() {
        screenModelScope.launch(Dispatchers.IO) {
            mutableState.update { it.copy(isDarkMode = !it.isDarkMode) }
            themeManagement.switchTheme(mutableState.value.isDarkMode)
        }
    }

    private fun getCurrentThemeMode() {
        tryToCollect(
            themeManagement::getThemeMode,
            ::onGetThemeModeSuccessfully,
            ::onError
        )
    }

    private fun onGetThemeModeSuccessfully(isDarkMode: Boolean) {
        updateState { it.copy(isDarkMode = isDarkMode) }
    }

}