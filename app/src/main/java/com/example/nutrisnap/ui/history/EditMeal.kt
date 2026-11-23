package com.example.nutrisnap.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.data.models.Meal

@Composable
fun EditMealDialog(
    meal: Meal,
    onDismiss: () -> Unit,
    onSave: (Meal) -> Unit
) {
    // Local text states pre-filled with the meal's current values
    var name by remember(meal.id) { mutableStateOf(meal.name) }
    var kcalText by remember(meal.id) { mutableStateOf(meal.kcal.toString()) }
    var proteinText by remember(meal.id) { mutableStateOf(meal.protein.toString()) }
    var carbsText by remember(meal.id) { mutableStateOf(meal.carbs.toString()) }
    var fatText by remember(meal.id) { mutableStateOf(meal.fat.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Meal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Meal name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = kcalText,
                    onValueChange = { kcalText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Calories (kcal)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = proteinText,
                    onValueChange = { proteinText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Protein (g)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = carbsText,
                    onValueChange = { carbsText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Carbs (g)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = fatText,
                    onValueChange = { fatText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Fat (g)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cleanName = name.trim().ifEmpty { meal.name }
                    val updated = meal.copy(
                        name = cleanName,
                        kcal = kcalText.toIntOrNull() ?: meal.kcal,
                        protein = proteinText.toIntOrNull() ?: meal.protein,
                        carbs = carbsText.toIntOrNull() ?: meal.carbs,
                        fat = fatText.toIntOrNull() ?: meal.fat
                    )
                    onSave(updated)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}