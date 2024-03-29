package presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.angus.designSystem.ui.composable.BpButton
import com.angus.designSystem.ui.composable.BpCheckBox
import com.angus.designSystem.ui.composable.BpTextField
import com.angus.designSystem.ui.composable.BpToastMessage
import com.angus.designSystem.ui.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.base.BaseScreen
import presentation.composables.CustomBottomSheet
import presentation.login.composable.HeadFirstCard
import presentation.login.composable.PermissionBottomSheetContent
import presentation.login.composable.WrongPermissionBottomSheet
import presentation.main.MainScreen
import presentation.restaurantSelection.RestaurantSelectionScreen
import resources.Resources
import util.getNavigationBarPadding

class LoginScreen :
    BaseScreen<LoginScreenModel, LoginScreenUIState, LoginScreenUIEffect, LoginScreenInteractionListener>() {

    @Composable
    override fun Content() {
        initScreen(getScreenModel())
    }

    override fun onEffect(
        effect: LoginScreenUIEffect,
        navigator: Navigator,
    ) {
        when (effect) {
            LoginScreenUIEffect.OnNavigateToMainScreen -> navigator.replaceAll(MainScreen())
            LoginScreenUIEffect.OnNavigateToRestaurantScreenSelection -> navigator.replaceAll(
                RestaurantSelectionScreen()
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun onRender(state: LoginScreenUIState, listener: LoginScreenInteractionListener) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomBottomSheet(
                sheetContent = {
                    if (state.showPermissionSheet) {
                        PermissionBottomSheetContent(
                            listener = listener,
                            state = state
                        )
                    } else {
                        WrongPermissionBottomSheet(
                            listener
                        )
                    }
                },
                sheetBackgroundColor = Theme.colors.background,
                onBackGroundClicked = listener::onSheetBackgroundClicked,
                sheetState = state.sheetState,
            ) {
                LoginScreenContent(state, listener)
            }
        }
        LaunchedEffect(state.showSnackbar) {
            if (state.showSnackbar) {
                delay(2000)
                listener.onDismissSnackBar()
            }
        }
        BpToastMessage(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .padding(bottom = getNavigationBarPadding().calculateBottomPadding()),
            state = state.showSnackbar,
            message = if (state.errorPermission == null)
                Resources.strings.yourRequestHasBeenSentSuccessfully else Resources.strings.somethingWentWrong,
            isError = state.errorPermission != null,
            successIcon = painterResource(Resources.images.unread),
            warningIcon = painterResource(Resources.images.errorIcon)
        )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun LoginScreenContent(
    state: LoginScreenUIState,
    listener: LoginScreenInteractionListener,
) {

    Box(
        modifier = Modifier.fillMaxSize().background(Theme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Resources.images.backgroundPattern),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        HeadFirstCard(
            textHeader = Resources.strings.welcomeToRestaurantApp,
            textSubHeader = Resources.strings.loginToAccessAllTheFeatures,
        ) {
            BpTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                text = state.userName,
                onValueChange = listener::onUserNameChanged,
                label = Resources.strings.username,
                keyboardType = KeyboardType.Text,
                errorMessage = if (state.isCredentialsError) Resources.strings.userNameIsRequired else state.usernameErrorMsg,
                isError = state.isUsernameError,
            )
            BpTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = state.password,
                onValueChange = listener::onPasswordChanged,
                label = Resources.strings.password,
                keyboardType = KeyboardType.Password,
                errorMessage = if (state.isCredentialsError) Resources.strings.passwordIsRequired else state.passwordErrorMsg,
                isError = state.isPasswordError

            )
            BpCheckBox(
                modifier = Modifier.padding(top = 16.dp),
                label = Resources.strings.keepMeLoggedIn,
                isChecked = state.keepLoggedIn,
                size = 24,
                textStyle = Theme.typography.caption,
                textColor = Theme.colors.contentSecondary,
                onCheck = listener::onKeepLoggedInClicked
            )
            BpButton(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                title = Resources.strings.login,
                onClick = {
                    listener.onClickLogin(
                        userName = state.userName,
                        password = state.password,
                        isKeepMeLoggedInChecked = state.keepLoggedIn
                    )
                }, isLoading = state.isLoading,
                enabled = state.isEnable
            )
        }
    }
}