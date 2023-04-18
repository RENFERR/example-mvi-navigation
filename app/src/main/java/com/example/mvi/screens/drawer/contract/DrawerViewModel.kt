package com.example.mvi.screens.drawer.contract

import androidx.lifecycle.viewModelScope
import com.example.data.repository.LoginStateRepository
import com.example.mvi.core.BaseViewModel
import com.example.mvi.screens.user.models.User
import com.example.mvi.screens.drawer.contract.DrawerContract.Effect
import com.example.mvi.screens.drawer.contract.DrawerContract.Intent
import com.example.mvi.screens.drawer.contract.DrawerContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val repository: LoginStateRepository
) : BaseViewModel<State, Intent, Effect>() {

    override fun createInitialState(): State = State.Loading
    override fun handleIntent(event: Intent): Unit = when (event) {
        Intent.Update -> getUser()
        Intent.Logout -> logout()
    }

    init {
        getUser()
    }

    private fun logout() {
        viewModelScope.launch { repository.logout() }
    }

    private fun getUser() {
        val exampleUser = User(
            firstName = "Kirill",
            secondName = "Maenkov",
            email = "someemail@gmail.com"
        )
        viewModelScope.launch {
            setState { State.Loading }
            delay(10000L)
            setState {
                State.Loaded(data = exampleUser)
            }
        }
    }
}