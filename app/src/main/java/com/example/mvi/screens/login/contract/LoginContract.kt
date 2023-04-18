package com.example.mvi.screens.login.contract

import com.example.mvi.core.ViewEffect
import com.example.mvi.core.ViewIntent
import com.example.mvi.core.ViewState
import java.lang.Exception

object LoginContract {

    sealed class State : ViewState {
        object Idle : State()
        object Loading : State()
        data class Failure(val exception: Exception) : State()
    }

    sealed class Effect : ViewEffect

    sealed class Intent : ViewIntent {
        object Login : Intent()
    }
}