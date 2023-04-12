package com.example.mvi.screens.list.contract

import com.example.mvi.core.ViewEffect
import com.example.mvi.core.ViewIntent
import com.example.mvi.core.ViewState
import com.example.mvi.utils.UiText

object ListContract {

    sealed class State : ViewState {
        object Loading : State()
        object Empty : State()
        data class Loaded(val data: List<String>) : State()
        data class Failure(val message: UiText) : State()
    }

    sealed class Intent : ViewIntent {
        object UpdateScreen : Intent()
    }

    sealed class Effect : ViewEffect
}