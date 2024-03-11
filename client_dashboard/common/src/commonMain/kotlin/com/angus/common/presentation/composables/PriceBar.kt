package com.angus.common.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import com.angus.common.presentation.util.kms

@Composable
fun PriceBar(
    priceLevel: Int,
    icon: Painter,
    iconColor: Color,
    modifier: Modifier = Modifier,
    iconsSize: Dp = 32.kms,
) {
    when {
        (priceLevel < 0) -> throw Exception("price level is smaller than 0")
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(-iconsSize / 2)
    ) {
        repeat(priceLevel) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(iconsSize)
            )
        }
    }
}
