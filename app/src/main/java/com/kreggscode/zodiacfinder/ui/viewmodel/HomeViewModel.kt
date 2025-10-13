package com.kreggscode.zodiacfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.zodiacfinder.data.model.DailyHoroscope
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val userSign: ZodiacSign? = null,
    val dailyHoroscope: DailyHoroscope? = null,
    val error: String? = null
)

class HomeViewModel(
    private val repository: ZodiacRepository = ZodiacRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun setUserSign(sign: ZodiacSign) {
        _uiState.value = _uiState.value.copy(userSign = sign)
        loadDailyHoroscope(sign)
    }
    
    fun loadDailyHoroscope(sign: ZodiacSign) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getDailyHoroscope(sign).fold(
                onSuccess = { horoscope ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        dailyHoroscope = horoscope
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load horoscope"
                    )
                }
            )
        }
    }
}
