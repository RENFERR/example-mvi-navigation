package com.example.mvi.screens.login.contract

import androidx.lifecycle.viewModelScope
import com.example.data.repository.LoginStateRepository
import com.example.mvi.core.BaseViewModel
import com.example.mvi.screens.login.contract.LoginContract.State
import com.example.mvi.screens.login.contract.LoginContract.Intent
import com.example.mvi.screens.login.contract.LoginContract.Effect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginStateRepository
) : BaseViewModel<State, Intent, Effect>() {

    override fun createInitialState(): State = State.Idle

    override fun handleIntent(event: Intent): Unit = when (event) {
        is Intent.Login -> login()
    }

    fun login() {
        viewModelScope.launch {
            setState {
                State.Loading
            }
            delay(2000L)
            repository.login()
        }
    }
}