package hu.yettel.highwayvignette.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.Vehicle
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val vehicle = repository.getVehicle()
                _uiState.update { it.copy(isLoading = false, vehicle = vehicle) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load vehicle data.") }
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val vehicle: Vehicle? = null,
    val error: String? = null
)