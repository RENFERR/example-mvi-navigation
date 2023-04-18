package com.example.mvi.screens.list.contract

import androidx.lifecycle.viewModelScope
import com.example.data.repository.LoginStateRepository
import com.example.mvi.core.BaseViewModel
import com.example.mvi.screens.list.contract.ListContract.Effect
import com.example.mvi.screens.list.contract.ListContract.Intent
import com.example.mvi.screens.list.contract.ListContract.State
import com.example.mvi.utils.Mapper.ResponseToState.toListState
import com.example.mvi.utils.NotAuthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: LoginStateRepository
) : BaseViewModel<State, Intent, Effect>() {

    init {
        getList()
    }

    override fun createInitialState(): State = State.Loading

    override fun handleIntent(event: Intent) {
        when (event) {
            Intent.UpdateScreen -> if (currentState !is State.Loading) getList()
        }
    }

    private fun getList() {
        viewModelScope.launch(dispatcher) {
            setState { State.Loading }
            delay(2000L)
            val safeResult = runCatching { exampleList() }
            if (safeResult.exceptionOrNull() is NotAuthorizedException) {
                repository.logout()
            } else {
                setState {
                    safeResult.toListState()
                }
            }
        }
    }

    private fun exampleList(): List<String> {
        return when ((0..5).random()) {
            0 -> throw NotAuthorizedException()
            1 -> throw Exception("Some exception message")
            else -> mutableListOf<String>().apply {
                repeat((0..20).random()) {
                    add(element = UUID.randomUUID().toString())
                }
            }
        }
    }
}