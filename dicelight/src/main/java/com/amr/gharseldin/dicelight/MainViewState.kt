package com.amr.gharseldin.dicelight

sealed class MainViewState {
    object Loading : MainViewState()
    data class Content(val passPhrase: String, val wordCount: Int): MainViewState()
    data class Error(val throwable: Throwable) : MainViewState()
}