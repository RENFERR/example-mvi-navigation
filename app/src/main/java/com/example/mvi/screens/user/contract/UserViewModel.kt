package com.example.mvi.screens.user.contract

import androidx.lifecycle.viewModelScope
import com.example.data.repository.LoginStateRepository
import com.example.mvi.core.BaseViewModel
import com.example.mvi.screens.user.contract.UserContract.Effect
import com.example.mvi.screens.user.contract.UserContract.Intent
import com.example.mvi.screens.user.contract.UserContract.State
import com.example.mvi.screens.user.models.User
import com.example.mvi.utils.Mapper.ResponseToState.toUserState
import com.example.mvi.utils.NotAuthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: LoginStateRepository
) : BaseViewModel<State, Intent, Effect>() {

    init {
        getUsername()
    }

    override fun createInitialState(): State = State.Idle

    override fun handleIntent(event: Intent) {
        when (event) {
            is Intent.UpdateScreen -> if (currentState !is State.Loading) getUsername()
        }
    }

    private fun getUsername() {
        viewModelScope.launch(context = dispatcher) {
            setState { State.Loading }
            delay(1000L)
            val safeResult = kotlin.runCatching { user() }
            if (safeResult.exceptionOrNull() is NotAuthorizedException) {
                repository.logout()
            } else {
                setState {
                    safeResult.toUserState()
                }
            }
        }
    }

    private fun user(): User = when ((0..5).random()) {
        0 -> throw NotAuthorizedException()
        1 -> throw Exception("Some exception message")
        else -> User(
            firstName = "Kirill",
            secondName = "Maenkov",
            email = "someemail@gmail.com"
        )
    }
}