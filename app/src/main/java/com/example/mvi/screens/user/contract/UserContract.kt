package com.example.mvi.screens.user.contract

import com.example.mvi.core.ViewEffect
import com.example.mvi.core.ViewIntent
import com.example.mvi.core.ViewState
import com.example.mvi.screens.user.models.User
import com.example.mvi.utils.UiText

object UserContract {

    sealed class State : ViewState {
        object Idle : State()
        object Loading : State()
        data class Loaded(val data: User?) : State()
        data class Failure(val message: UiText) : State()
    }

    sealed class Intent : ViewIntent {
        object UpdateScreen : Intent()
    }

    sealed class Effect : ViewEffect
}