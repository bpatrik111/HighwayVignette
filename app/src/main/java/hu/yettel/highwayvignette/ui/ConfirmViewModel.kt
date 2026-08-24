package hu.yettel.highwayvignette.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ConfirmState>(ConfirmState.Idle)
    val state: StateFlow<ConfirmState> = _state.asStateFlow()

    fun confirmOrder(item: OrderLineItem) {
        if (_state.value == ConfirmState.Loading) return

        _state.update { ConfirmState.Loading }
        viewModelScope.launch {
            when (val result = repository.placeOrder(listOf(item))) {
                is OrderResult.Success -> _state.update { ConfirmState.Success }
                is OrderResult.Failure -> _state.update { ConfirmState.Error(result.message) }
            }
        }
    }
}

sealed interface ConfirmState {
    data object Idle : ConfirmState
    data object Loading : ConfirmState
    data object Success : ConfirmState
    data class Error(val message: String) : ConfirmState
}