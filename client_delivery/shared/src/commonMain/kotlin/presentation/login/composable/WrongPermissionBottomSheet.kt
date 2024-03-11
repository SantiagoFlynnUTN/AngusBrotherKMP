package presentation.login.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.angus.designSystem.ui.composable.BpButton
import com.angus.designSystem.ui.composable.BpTransparentButton
import com.angus.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.login.PermissionInteractionListener
import resources.Resources

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WrongPermissionBottomSheet(listener: PermissionInteractionListener,
                               modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentHeight()
            .padding(horizontal = 16.dp, vertical =24.dp)
    ) {
        Icon(
            modifier = Modifier.background(
                color = Theme.colors.hover,
                shape = RoundedCornerShape(Theme.radius.medium)
            ).padding(8.dp),
            painter = painterResource(Resources.images.warningIcon),
            tint = Theme.colors.primary,
            contentDescription = null
        )
        Text(
            text = Resources.strings.wrongPermission,
            style = Theme.typography.titleLarge,
            color = Theme.colors.contentPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = Resources.strings.wrongPermissionMessage,
            style = Theme.typography.body,
            color = Theme.colors.contentSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp, start = 32.dp,end=32.dp)
        )
        BpButton(
            onClick = listener::onRequestPermissionClicked,
            title = Resources.strings.requestAPermission,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        BpTransparentButton(
            onClick = listener::onCancelClicked,
            title = Resources.strings.close,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
    }
}