package com.angus.common.presentation.app

import LoginScreen
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.angus.designSystem.ui.theme.BpTheme
import org.koin.java.KoinJavaComponent.inject
import com.angus.common.presentation.resources.ProvideResources

@Composable
fun App() {

    val appScreenModel by remember { inject<AppScreenModel>(AppScreenModel::class.java) }
    val themeMode by appScreenModel.state.collectAsState()
    BpTheme(useDarkTheme = themeMode) {
        ProvideResources(isSystemInDarkTheme = themeMode) {
            Navigator(LoginScreen()) {
                SlideTransition(it)
            }
        }
    }
}
