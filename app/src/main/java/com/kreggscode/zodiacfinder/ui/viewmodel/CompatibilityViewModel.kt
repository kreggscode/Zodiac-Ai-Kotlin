package com.kreggscode.zodiacfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.zodiacfinder.data.model.CompatibilityResult
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CompatibilityUiState(
    val isLoading: Boolean = false,
    val sign1: ZodiacSign? = null,
    val sign2: ZodiacSign? = null,
    val result: CompatibilityResult? = null,
    val error: String? = null
)

class CompatibilityViewModel(
    private val repository: ZodiacRepository = ZodiacRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CompatibilityUiState())
    val uiState: StateFlow<CompatibilityUiState> = _uiState.asStateFlow()
    
    fun selectSign1(sign: ZodiacSign) {
        _uiState.value = _uiState.value.copy(sign1 = sign)
        checkCompatibilityIfReady()
    }
    
    fun selectSign2(sign: ZodiacSign) {
        _uiState.value = _uiState.value.copy(sign2 = sign)
        checkCompatibilityIfReady()
    }
    
    private fun checkCompatibilityIfReady() {
        val sign1 = _uiState.value.sign1
        val sign2 = _uiState.value.sign2
        
        if (sign1 != null && sign2 != null) {
            checkCompatibility(sign1, sign2)
        }
    }
    
    fun checkCompatibility(sign1: ZodiacSign, sign2: ZodiacSign) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.checkCompatibility(sign1, sign2).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to check compatibility"
                    )
                }
            )
        }
    }
    
    fun reset() {
        _uiState.value = CompatibilityUiState()
    }
}
