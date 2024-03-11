package presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.angus.designSystem.ui.composable.BpChip


@Composable
fun VariationsChips(modifier: Modifier, options: List<String>) {
    // Remember a mutable state to track the currently selected option
    val (selectedOption, setSelectedOption) = remember { mutableStateOf<String?>(null) }

    Row(modifier = modifier.selectableGroup()) {
        options.forEach { option ->
            BpChip(
                label = option,
                isSelected = option == selectedOption,
                onClick = {
                    // Instead of directly setting the selected option,
                    // we trigger re-composition with the new selection state.
                    setSelectedOption(option)
                },
                modifier = Modifier
                    .padding(4.dp)
                // The .selectable() modifier is not needed here since the onClick in BpChip handles it.
            )
        }
    }
}

@Composable
fun PreviewVariationsChips() {
    VariationsChips(Modifier.padding(start = 8.dp), options = listOf("Chip 1", "Chip 2", "Chip 3"))
}