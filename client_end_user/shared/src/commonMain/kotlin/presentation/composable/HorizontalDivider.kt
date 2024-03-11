package presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.angus.designSystem.ui.theme.Theme

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = Theme.colors.divider,
    orderHasFinished: Boolean = false
) {
    val animatedColor = if (orderHasFinished) Theme.colors.primary else color
    Box(modifier.height(thickness).background(animatedColor))
}