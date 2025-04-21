package com.example.unittestsdemo.amiibo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmiiboViewModel
@Inject
constructor(
    private val getAmiibosUseCase: GetAmiibosUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AmiiboState>(AmiiboState.Loading)
    val state: StateFlow<AmiiboState> = _state

    private val _intent = MutableSharedFlow<AmiiboIntent>()
    private val intent: SharedFlow<AmiiboIntent> = _intent

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collect { userIntent ->
                when (userIntent) {
                    is AmiiboIntent.FetchAmiibos -> fetchAmiibos(userIntent.name)
                }
            }
        }
    }

    private fun fetchAmiibos(name: String) {
        viewModelScope.launch {
            getAmiibosUseCase(name)
                .catch {
                    _state.emit(AmiiboState.Error)
                }
                .collect {
                    _state.emit(AmiiboState.Success(it))
                }
        }
    }

    fun sendIntent(intent: AmiiboIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
}
