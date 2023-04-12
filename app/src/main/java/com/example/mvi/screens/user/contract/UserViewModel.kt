package com.example.mvi.screens.user.contract

import androidx.lifecycle.viewModelScope
import com.example.mvi.core.BaseViewModel
import com.example.mvi.screens.user.contract.UserContract.Effect
import com.example.mvi.screens.user.contract.UserContract.Intent
import com.example.mvi.screens.user.contract.UserContract.State
import com.example.mvi.utils.Mapper.ResponseToState.toUserState
import com.example.mvi.utils.NotAuthorizedException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel<State, Intent, Effect>() {

    init {
        getUsername()
    }

    override fun createInitialState(): State = State.Idle

    override fun handleIntent(event: Intent): Unit = when (event) {
        is Intent.UpdateScreen -> getUsername()
    }

    private fun getUsername() {
        viewModelScope.launch(context = dispatcher) {
            setState { State.Loading }
            delay(1000L)
            val safeResult = kotlin.runCatching { username() }
            safeResult.setState { response ->
                response.toUserState()
            }
        }
    }

    private fun username(): String = when ((0..3).random()) {
        0 -> throw NotAuthorizedException()
        1 -> throw Exception("Some exception message")
        else -> "MyUsername"
    }
}