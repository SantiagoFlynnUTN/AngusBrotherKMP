package presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import presentation.login.LoginScreen
import resources.AngusTheme

@Composable
fun App() {
    MainApp.Content()
}

object MainApp : Screen {
    @Composable
    override fun Content() {
        val appScreenModel = getScreenModel<AppScreenModel>()

        val isKeptLoggedIn by appScreenModel.isKeptLoggedIn.collectAsState()

        AngusTheme{
            Navigator(LoginScreen()) { SlideTransition(it) }
            /*if (isKeptLoggedIn==true) {
                Navigator(MainScreen()) { SlideTransition(it) }
            } else if(isKeptLoggedIn == false){
                Navigator(LoginScreen()) { SlideTransition(it) }
            }*/
        }
    }
}
