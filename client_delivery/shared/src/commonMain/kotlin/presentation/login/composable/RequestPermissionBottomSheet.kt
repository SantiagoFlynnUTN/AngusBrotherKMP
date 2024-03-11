package presentation.login.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.angus.designSystem.ui.composable.BpButton
import com.angus.designSystem.ui.composable.BpExpandableTextField
import com.angus.designSystem.ui.composable.BpTextField
import com.angus.designSystem.ui.composable.BpTransparentButton
import com.angus.designSystem.ui.theme.Theme
import presentation.login.LoginScreenUIState
import presentation.login.PermissionInteractionListener
import resources.Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPermissionBottomSheet(
    listener: PermissionInteractionListener,
    state: LoginScreenUIState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = Resources.strings.askForPermission,
            color = Theme.colors.contentPrimary,
            style = Theme.typography.titleLarge,
        )
        BpTextField(
            text = state.permissionUiState.deliveryUsername,
            label = Resources.strings.deliveryUsername,
            keyboardType = KeyboardType.Text,
            onValueChange = listener::onRestaurantNameChanged,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
        BpTextField(
            text = state.permissionUiState.ownerEmail,
            onValueChange = listener::onOwnerEmailChanged,
            label = Resources.strings.userEmailLabel,
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )
        BpExpandableTextField(
            text = state.permissionUiState.description,
            onValueChange = listener::onDescriptionChanged,
            label = Resources.strings.whyAngusBrother,
            hint = Resources.strings.questionHint,
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )
        BpButton(
            onClick = {
                listener.onSubmitClicked(
                    restaurantName = state.permissionUiState.deliveryUsername,
                    ownerEmail = state.permissionUiState.ownerEmail,
                    description = state.permissionUiState.description
                )
            },
            title = Resources.strings.submit,
            modifier = Modifier.fillMaxWidth().padding(top =24.dp),
        )
        BpTransparentButton(
            onClick = listener::onCancelClicked,
            title = Resources.strings.cancel,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        )
    }
}