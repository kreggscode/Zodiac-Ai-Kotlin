package com.kreggscode.zodiacfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.zodiacfinder.data.api.MessageRequest
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(
    private val repository: ZodiacRepository = ZodiacRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState(
        messages = listOf(
            ChatMessage(
                content = "Hello! I'm your personal astrology guide. Ask me anything about zodiac signs, horoscopes, compatibility, or spiritual guidance.",
                isUser = false
            )
        )
    ))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun sendMessage(message: String) {
        if (message.isBlank()) return
        
        // Add user message
        val userMessage = ChatMessage(content = message, isUser = true)
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true,
            error = null
        )
        
        viewModelScope.launch {
            // Convert chat history to API format
            val conversationHistory = _uiState.value.messages
                .dropLast(1) // Exclude the just-added user message
                .filter { it.isUser || it.content.isNotBlank() }
                .map { MessageRequest(if (it.isUser) "user" else "assistant", it.content) }
            
            repository.chatWithAI(message, conversationHistory).fold(
                onSuccess = { response ->
                    val aiMessage = ChatMessage(content = response, isUser = false)
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + aiMessage,
                        isLoading = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to get response"
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
