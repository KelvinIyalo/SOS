package com.example.sosapplication.model

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class DisplayError(val error: String) : UiState<Nothing>()
    data class ApiError(val error: String) : UiState<Nothing>()
}