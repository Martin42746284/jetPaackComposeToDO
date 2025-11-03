package com.example.jettodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jettodo.model.Priority
import com.example.jettodo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    todoId: Int?,
    initialTitle: String = "",
    initialLabel: String = "",
    initialPriority: Priority = Priority.LOW,
    onSave: (String, String, Priority) -> Unit,
    onClose: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var selectedLabel by remember { mutableStateOf(initialLabel) }
    var selectedPriority by remember { mutableStateOf(initialPriority) }
    var expandedLabelMenu by remember { mutableStateOf(false) }

    val labels = listOf("Select Label", "Home", "Food", "Music", "Work", "Personal")
    val isEditMode = todoId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Todo" else "Add Todo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
        ) {
            // ✅ Contenu scrollable
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Title Input
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What needs to be done?", color = TextHint) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Label Dropdown
                Text(
                    text = "Label",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedLabel.ifEmpty { "Select Label" },
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedLabelMenu = true },
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = if (selectedLabel.isEmpty()) TextHint else TextPrimary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = BorderLight,
                            disabledContainerColor = SurfaceWhite,
                            disabledTextColor = if (selectedLabel.isEmpty()) TextHint else TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    DropdownMenu(
                        expanded = expandedLabelMenu,
                        onDismissRequest = { expandedLabelMenu = false },
                        modifier = Modifier.background(SurfaceWhite)
                    ) {
                        labels.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = label,
                                        color = if (label == "Select Label") TextHint else TextPrimary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                onClick = {
                                    if (label != "Select Label") {
                                        selectedLabel = label
                                    }
                                    expandedLabelMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Priority Selection
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Priority.entries.forEach { priority ->
                        PriorityRadioButton(
                            text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
                            selected = selectedPriority == priority,
                            onClick = { selectedPriority = priority },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Button(
                onClick = {
                    if (title.isNotBlank() && selectedLabel.isNotEmpty() && selectedLabel != "Select Label") {
                        onSave(title, selectedLabel, selectedPriority)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)  // ✅ Padding autour du bouton
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && selectedLabel.isNotEmpty() && selectedLabel != "Select Label"
            ) {
                Text(
                    text = if (isEditMode) "Update" else "Done",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PriorityRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryBlue,
                unselectedColor = TextSecondary
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
    }
}