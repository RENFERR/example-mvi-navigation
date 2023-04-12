package com.example.mvi.screens.list.contract

import androidx.lifecycle.viewModelScope
import com.example.mvi.core.BaseViewModel
import com.example.mvi.core.Response
import com.example.mvi.screens.list.contract.ListContract.Effect
import com.example.mvi.screens.list.contract.ListContract.Intent
import com.example.mvi.screens.list.contract.ListContract.State
import com.example.mvi.utils.Mapper.ResponseToState.toListState
import com.example.mvi.utils.NotAuthorizedException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ListViewModel : BaseViewModel<State, Intent, Effect>() {

    init {
        getList()
    }

    override fun createInitialState(): State = State.Loading

    override fun handleIntent(event: Intent): Unit = when (event) {
        Intent.UpdateScreen -> getList()
    }

    private fun getList() {
        viewModelScope.launch(dispatcher) {
            setState { State.Loading }
            delay(2000L)
            val safeResponse = Response.safeCall(call = { exampleList() })
            safeResponse.setState { response ->
                response.toListState()
            }
        }
    }

    private fun exampleList(): List<String> {
        return when ((0..4).random()) {
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