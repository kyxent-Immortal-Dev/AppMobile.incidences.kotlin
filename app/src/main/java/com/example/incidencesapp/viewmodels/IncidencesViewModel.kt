
// viewmodels/IncidencesViewModel.kt
package com.example.incidencesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incidencesapp.models.Incidence
import com.example.incidencesapp.models.IncidenceRequest
import com.example.incidencesapp.repository.IncidencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IncidencesViewModel(
    private val repository: IncidencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncidencesUiState())
    val uiState: StateFlow<IncidencesUiState> = _uiState.asStateFlow()

    fun loadAllIncidences() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getAllIncidences().collect { result ->
                result.fold(
                    onSuccess = { incidences ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            incidences = incidences,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                )
            }
        }
    }

    fun loadMyIncidences() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getMyIncidences().collect { result ->
                result.fold(
                    onSuccess = { incidences ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            incidences = incidences,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                )
            }
        }
    }

    fun createIncidence(incidence: IncidenceRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.createIncidence(incidence).collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        loadMyIncidences() // Recargar lista
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                )
            }
        }
    }

    fun updateIncidence(id: String, incidence: IncidenceRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.updateIncidence(id, incidence).collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        loadMyIncidences() // Recargar lista
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                )
            }
        }
    }

    fun deleteIncidence(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.deleteIncidence(id).collect { result ->
                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        loadMyIncidences() // Recargar lista
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class IncidencesUiState(
    val isLoading: Boolean = false,
    val incidences: List<Incidence> = emptyList(),
    val error: String? = null
)