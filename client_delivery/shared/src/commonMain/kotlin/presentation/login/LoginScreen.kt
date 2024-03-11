package presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.angus.designSystem.ui.composable.BPSnackBar
import com.angus.designSystem.ui.composable.BpButton
import com.angus.designSystem.ui.composable.BpCheckBox
import com.angus.designSystem.ui.composable.BpTextField
import com.angus.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.base.BaseScreen
import presentation.login.composable.LoginScaffold
import presentation.login.composable.RequestPermissionBottomSheet
import presentation.login.composable.WrongPermissionBottomSheet
import presentation.main.MainScreen
import resources.Resources
import util.getNavigationBarPadding

class LoginScreen : BaseScreen<
        LoginScreenModel,
        LoginScreenUIState,
        LoginScreenUIEffect,
        LoginScreenInteractionListener>() {

    @Composable
    override fun onRender(state: LoginScreenUIState, listener: LoginScreenInteractionListener) {
        Column(modifier = Modifier.fillMaxSize()) {
            LoginScaffold(
                sheetContent = {
                    if (state.permissionUiState.showPermissionSheet) {
                        RequestPermissionBottomSheet(
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
                sheetState = state.permissionUiState.sheetState,
            ) {
                LoginScreenContent(state, listener)
            }
        }
    }

    override fun onEffect(effect: LoginScreenUIEffect, navigator: Navigator) {
          when (effect) {
            is LoginScreenUIEffect.LoginEffect -> navigator.replaceAll(MainScreen())
            LoginScreenUIEffect.LoginUIFailed -> {}
        }
    }

    @Composable
    override fun Content() {
        initScreen(getScreenModel())
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun LoginScreenContent(
    state: LoginScreenUIState,
    listener: LoginScreenInteractionListener
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Theme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Resources.images.backgroundPattern),
            contentDescription = "background Image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(Theme.radius.medium))
                .background(Theme.colors.surface)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(width = 70.dp, height = 70.dp),
                painter = painterResource(Resources.images.angusLogo),
                contentDescription = "bpLogo",
                tint = Theme.colors.primary
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp, top = 32.dp),
                text = Resources.strings.loginWelcomeMessage,
                style = Theme.typography.titleLarge,
                color = Theme.colors.contentPrimary
            )
            Text(
                text = Resources.strings.loginSubWelcomeMessage,
                style = Theme.typography.body,
                color = Theme.colors.contentTertiary
            )
            BpTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                text = state.userName,
                onValueChange = listener::onUserNameChanged,
                label = Resources.strings.usernameLabel,
                errorMessage = if (state.isUsernameError) Resources.strings.invalidUsername else "",
                isError = state.isUsernameError,
            )
            BpTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = state.password,
                onValueChange = listener::onPasswordChanged,
                label = Resources.strings.passwordLabel,
                keyboardType = KeyboardType.Password,
                errorMessage = if (state.isPasswordError) Resources.strings.invalidPassword else "",
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
                title = Resources.strings.login,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
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

         BPSnackBar(
                icon = painterResource(Resources.images.warningIcon),
                iconBackgroundColor = Theme.colors.warningContainer,
                iconTint = Theme.colors.warning,
                isVisible = state.showSnackBar,
                modifier = Modifier.padding(bottom = getNavigationBarPadding().calculateBottomPadding())
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = Resources.strings.signupWithAngusBrother,
                    style = Theme.typography.body.copy(color = Theme.colors.contentPrimary),
                )
        }
    }
}