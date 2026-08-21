package hu.yettel.highwayvignette.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.domain.model.CountyAdjacency
import hu.yettel.highwayvignette.domain.model.VignetteOption
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CountySelectionViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountySelectionUiState())
    val uiState: StateFlow<CountySelectionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val counties = repository.getCounties()
            val price = repository.getCountyVignettePrice()
            _uiState.update { it.copy(isLoading = false, counties = counties, unitPrice = price) }
        }
    }

    fun toggleCounty(countyId: String) {
        _uiState.update { state ->
            val current = state.selectedIds
            if (current.contains(countyId)) {
                state.copy(selectedIds = current - countyId, warning = null)
            } else {
                val connected = CountyAdjacency.isDirectlyConnected(countyId, current)
                state.copy(
                    selectedIds = current + countyId,
                    warning = if (!connected) {
                        "Ez a vármegye nem határos közvetlenül a jelenlegi kiválasztással."
                    } else {
                        null
                    }
                )
            }
        }
    }
}

data class CountySelectionUiState(
    val isLoading: Boolean = true,
    val counties: List<County> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val warning: String? = null,
    val unitPrice: VignetteOption? = null
) {
    val total: Double
        get() = (unitPrice?.sum ?: 0.0) * selectedIds.size
}