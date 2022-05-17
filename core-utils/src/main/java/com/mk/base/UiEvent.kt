package com.mk.base

sealed class UiEvent {
    object Success: UiEvent()
    data class Error(val text: String): UiEvent()
}
