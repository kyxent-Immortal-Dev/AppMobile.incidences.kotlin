// screens/CreateEditIncidenceScreen.kt
package com.example.incidencesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incidencesapp.R
import com.example.incidencesapp.models.IncidenceRequest
import com.example.incidencesapp.repository.IncidencesRepository
import com.example.incidencesapp.ui.theme.AppIcons
import com.example.incidencesapp.utils.SessionManager
import com.example.incidencesapp.viewmodels.IncidencesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditIncidenceScreen(
    incidenceId: String? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { IncidencesRepository(sessionManager) }
    val viewModel = remember { IncidencesViewModel(repository) }

    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Baja") }
    var status by remember { mutableStateOf("Pendiente") }
    var showPriorityDropdown by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }

    val isEditing = incidenceId != null
    val priorities = listOf("Baja", "Media", "Alta")
    val statuses = listOf("Pendiente", "En Progreso", "Completado")

    // Si estamos editando, busca la incidencia en la lista y carga los datos
    val incidenceToEdit = uiState.incidences.find { it._id == incidenceId }
    LaunchedEffect(incidenceToEdit) {
        incidenceToEdit?.let { incidence ->
            title = incidence.title
            description = incidence.description
            priority = incidence.priority
            status = incidence.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Incidencia" else "Nueva Incidencia")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = AppIcons.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.incidence_title)) },
                leadingIcon = {
                    Icon(
                        imageVector = AppIcons.Description,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.incidence_description)) },
                leadingIcon = {
                    Icon(
                        imageVector = AppIcons.Description,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Selector de Prioridad
            ExposedDropdownMenuBox(
                expanded = showPriorityDropdown,
                onExpandedChange = { showPriorityDropdown = !showPriorityDropdown }
            ) {
                OutlinedTextField(
                    value = priority,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.incidence_priority)) },
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.PriorityHigh,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriorityDropdown)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = showPriorityDropdown,
                    onDismissRequest = { showPriorityDropdown = false }
                ) {
                    priorities.forEach { priorityOption ->
                        DropdownMenuItem(
                            text = { Text(priorityOption) },
                            onClick = {
                                priority = priorityOption
                                showPriorityDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Estado
            ExposedDropdownMenuBox(
                expanded = showStatusDropdown,
                onExpandedChange = { showStatusDropdown = !showStatusDropdown }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.incidence_status)) },
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.Schedule,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showStatusDropdown)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = showStatusDropdown,
                    onDismissRequest = { showStatusDropdown = false }
                ) {
                    statuses.forEach { statusOption ->
                        DropdownMenuItem(
                            text = { Text(statusOption) },
                            onClick = {
                                status = statusOption
                                showStatusDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            uiState.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { viewModel.clearError() }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }

            val isFormValid = title.isNotBlank() && description.isNotBlank() && priority.isNotBlank() && status.isNotBlank()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.incidence_cancel))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (isFormValid) {
                            val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
                            val incidenceRequest = IncidenceRequest(
                                title = title,
                                description = description,
                                priority = priority,
                                status = status,
                                date = currentDate
                            )

                            if (isEditing && incidenceId != null) {
                                viewModel.updateIncidence(incidenceId, incidenceRequest)
                            } else {
                                viewModel.createIncidence(incidenceRequest)
                            }

                            if (!uiState.isLoading) {
                                onNavigateBack()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading && isFormValid
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.incidence_save))
                    }
                }
            }
        }
    }
}