package com.somadhan.fiberlasercalculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun CustomDropdownMenu(
    splitType: String,
    onSplitTypeChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (splitType == "ratio") "Splitter Ratio (1:n)" else "Split Percentage (%)")
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)  // Added Dropdown icon
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onSplitTypeChange("ratio")
                    expanded = false
                },
                text = {
                    Text(text = "Splitter Ratio (1:n)")
                }
            )
            DropdownMenuItem(
                onClick = {
                    onSplitTypeChange("percentage")
                    expanded = false
                },
                text = {
                    Text(text = "Split Percentage (%)")
                }
            )
        }
    }
}
