package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.todolistapp.ui.theme.ToDoListAppTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ToDoListScreen()
                }
            }
        }
    }
}

data class TaskItem(
    val text: String,
    val date: String,
    var isChecked: Boolean = false
)

@Composable
fun ToDoListScreen() {
    var taskText by remember { mutableStateOf(TextFieldValue("")) }
    var tasks by remember { mutableStateOf(listOf<TaskItem>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("To-Do List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                label = { Text("Tambah tugas...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (taskText.text.isNotBlank()) {
                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    tasks = tasks + TaskItem(taskText.text, date)
                    taskText = TextFieldValue("")
                }
            }) {
                Text("Tambah")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks.size) { index ->
                var isEditing by remember { mutableStateOf(false) }
                var editedText by remember { mutableStateOf(tasks[index].text) }

                val task = tasks[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = task.isChecked,
                                onCheckedChange = { isChecked ->
                                    tasks = tasks.toMutableList().also {
                                        it[index] = it[index].copy(isChecked = isChecked)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            if (isEditing) {
                                OutlinedTextField(
                                    value = editedText,
                                    onValueChange = { editedText = it },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Column {
                                    Text(task.text, style = MaterialTheme.typography.bodyLarge)
                                    Text("Dibuat: ${task.date}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        Row {
                            if (isEditing) {
                                Button(onClick = {
                                    if (editedText.isNotBlank()) {
                                        tasks = tasks.toMutableList().also {
                                            it[index] = it[index].copy(text = editedText)
                                        }
                                    }
                                    isEditing = false
                                }) {
                                    Text("Save")
                                }
                            } else {
                                Button(onClick = {
                                    isEditing = true
                                    editedText = task.text
                                }) {
                                    Text("Edit")
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(onClick = {
                                tasks = tasks.toMutableList().also { it.removeAt(index) }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus")
                            }
                        }
                    }
                }
            }
        }
    }
}
