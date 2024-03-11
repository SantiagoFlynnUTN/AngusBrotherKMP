package com.angus.designSystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.platform.Font

@Composable
actual fun fontResources(font: String): Font = Font("font/$font.ttf")
