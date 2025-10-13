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

data class HoroscopeUiState(
    val isLoading: Boolean = false,
    val selectedSign: ZodiacSign = ZodiacSign.ARIES,
    val horoscope: DailyHoroscope? = null,
    val error: String? = null
)

class HoroscopeViewModel(
    private val repository: ZodiacRepository = ZodiacRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HoroscopeUiState())
    val uiState: StateFlow<HoroscopeUiState> = _uiState.asStateFlow()
    
    fun selectSign(sign: ZodiacSign) {
        _uiState.value = _uiState.value.copy(selectedSign = sign)
        loadHoroscope(sign)
    }
    
    fun loadHoroscope(sign: ZodiacSign) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getDailyHoroscope(sign).fold(
                onSuccess = { horoscope ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        horoscope = horoscope
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
    
    init {
        loadHoroscope(ZodiacSign.ARIES)
    }
}
