package com.example.mvi.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi.utils.NotAuthorizedException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<
        State : ViewState,
        Intent : ViewIntent,
        Effect : ViewEffect>(
    protected val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    protected val currentState: State
        get() = state.value

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _event: MutableSharedFlow<Intent> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    private val _isLogged: Channel<Boolean> = Channel()
    val isLogged = _isLogged.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch(dispatcher) {
            event.collect {
                handleIntent(it)
            }
        }
    }

    abstract fun handleIntent(event: Intent)

    fun setEvent(event: Intent) {
        val newEvent = event
        viewModelScope.launch { _event.emit(newEvent) }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _state.value = newState
    }

    protected fun <T> Response<T>.setState(
        reduce: State.(Response<T>) -> State
    ) {
        if (this is Response.Failure && this.exception is NotAuthorizedException) {
            viewModelScope.launch { _isLogged.send(element = false) }
        } else {
            _state.value = currentState.reduce(this)
        }
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}