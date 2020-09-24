package com.amr.gharseldin.dicelight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DEFAULT_WORD_COUNT = 6

class MainMotor(application: Application) : AndroidViewModel(application) {
    private val _results = MutableLiveData<MainViewState>()
    val results: LiveData<MainViewState> = _results

    init {
        generatePassphrase(DEFAULT_WORD_COUNT)
    }

    fun generatePassphrase() {
        generatePassphrase(
            (results.value as? MainViewState.Content)?.wordCount ?: DEFAULT_WORD_COUNT
        )
    }

    fun generatePassphrase(wordCount: Int) {
        _results.value = MainViewState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            _results.value = try {
                val randomWords = PassphraseRepository.generate(
                    getApplication(),
                    wordCount
                )
                MainViewState.Content(randomWords.joinToString(" "), wordCount)
            } catch (t: Throwable) {
                MainViewState.Error(t)
            }
        }
    }

}