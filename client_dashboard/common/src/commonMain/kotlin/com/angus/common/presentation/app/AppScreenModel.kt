package com.angus.common.presentation.app

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.angus.common.domain.usecase.IManageThemeUseCase

class AppScreenModel(
    private val themeManagement: IManageThemeUseCase
) : StateScreenModel<Boolean>(false) {

    init {
        getThemeMode()
    }

    private fun getThemeMode() {
        screenModelScope.launch(Dispatchers.IO) {
            themeManagement.getThemeMode().collect { isDarkTheme ->
                mutableState.update { true }
            }
        }
    }

}