package hu.yettel.highwayvignette.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CountyConfirmViewModel @Inject constructor(
    private val repository: HighwayRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedIds: List<String> =
        savedStateHandle.get<String>("countyIds").orEmpty().split(",").filter { it.isNotBlank() }

    private val _state = MutableStateFlow(CountyConfirmUiState())
    val state: StateFlow<CountyConfirmUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val allCounties = repository.getCounties()
            val price = repository.getCountyVignettePrice()
            val selectedCounties = allCounties.filter { it.id in selectedIds }
            _state.update {
                it.copy(
                    isLoading = false,
                    selectedCounties = selectedCounties,
                    unitPrice = price,
                    total = price.sum * selectedCounties.size
                )
            }
        }
    }

    fun confirmOrder() {
        val price = _state.value.unitPrice ?: return
        _state.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val items = _state.value.selectedCounties.map {
                OrderLineItem(type = it.id, category = "CAR", cost = price.cost)
            }
            when (val result = repository.placeOrder(items)) {
                is OrderResult.Success -> _state.update { it.copy(isSubmitting = false, isSuccess = true) }
                is OrderResult.Failure -> _state.update { it.copy(isSubmitting = false, error = result.message) }
            }
        }
    }
}

data class CountyConfirmUiState(
    val isLoading: Boolean = true,
    val selectedCounties: List<County> = emptyList(),
    val unitPrice: hu.yettel.highwayvignette.domain.model.VignetteOption? = null,
    val total: Double = 0.0,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)