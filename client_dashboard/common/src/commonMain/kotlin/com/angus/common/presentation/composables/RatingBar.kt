package com.angus.common.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.angus.common.presentation.util.kms
import kotlin.math.ceil
import kotlin.math.floor


@Composable
fun RatingBar(
    rating: Double,
    selectedIcon: Painter,
    halfSelectedIcon: Painter,
    modifier: Modifier = Modifier,
    iconsSize: Dp = 24.kms,
    iconsPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
) {
    when {
        (rating < 0.0) -> throw Exception("rating is smaller than 0")
    }
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(if (rating.rem(1) < 0.5) floor(rating).toInt() else ceil(rating).toInt()) { position ->
            Image(
                painter = when {
                    position < floor(rating) ||
                            (position == floor(rating).toInt() && rating.rem(1) > 0.89) -> selectedIcon

                    else -> halfSelectedIcon
                },
                contentDescription = null,
                modifier = Modifier.padding(iconsPadding).size(iconsSize)
            )
        }
    }
}