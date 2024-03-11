package com.angus.common.presentation.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.angus.common.presentation.resources.Resources

@Composable
fun BpLogo(expanded: Boolean, modifier: Modifier = Modifier) {
    Box(modifier) {
        Crossfade(expanded) { targetState ->
            Image(
                painterResource(
                    if (targetState) Resources.Drawable.angusLogoExpanded else Resources.Drawable.angusLogo
                ),
                contentDescription = null,
                alignment = Alignment.CenterStart,
            )
        }
    }
}